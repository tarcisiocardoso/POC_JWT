package com.example.demo.config.jwt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import javax.naming.ldap.LdapName;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/authenticate1", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken1(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        System.out.println(">>>createAuthenticationToken<<<");
        Authentication authentication = authenticate(authenticationRequest.getUsername(),
                authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        // final String token = jwtTokenUtil.generateToken(authentication);

        // Authentication authentication = authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(
        // loginRequest.getUsername(),
        // loginRequest.getPassword()
        // )
        // );

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Autowired
    LdapTemplate ldapTemplate;

    @GetMapping("/userInfo")
    public ResponseEntity<Object> userInfo(HttpServletRequest request) throws Exception {

        final String requestTokenHeader = request.getHeader("Authorization");

        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        // System.out.println( "Nome: "+ user.getName() );
        // System.out.println( user.getPrincipal() +":
        // "+user.getPrincipal().getClass().getName());
        // System.out.println( user.getCredentials() +":
        // "+user.getCredentials().getClass().getName() );

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                List<Person> lstP = findByUID(username);
                if (lstP.size() > 0) {
                    return ResponseEntity.ok(lstP.get(0));
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }
        List<Person> lstP = findByUID(username);

        lstP.forEach(p -> System.out.println(p));

        return ResponseEntity.ok(user.getPrincipal());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        final String jwt = jwtTokenUtil.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    private Authentication authenticate(String username, String password) throws Exception {
        System.out.println(">>>authenticate<<<");
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    private List<Person> findByUID(String uid) {
        return ldapTemplate.search(query().where("uid").is(uid), PERSON_CONTEXT_MAPPER);
    }

    private final static ContextMapper<Person> PERSON_CONTEXT_MAPPER = new AbstractContextMapper<Person>() {
        @Override
        public Person doMapFromContext(DirContextOperations context) {
            Person person = new Person();

            LdapName dn = LdapUtils.newLdapName(context.getDn());
            person.setCountry(LdapUtils.getStringValue(dn, 0));
            person.setCompany(LdapUtils.getStringValue(dn, 1));
            person.setFullName(context.getStringAttribute("cn"));
            person.setLastName(context.getStringAttribute("sn"));
            person.setDescription(context.getStringAttribute("description"));
            person.setPhone(context.getStringAttribute("telephoneNumber"));

            return person;
        }
    };

}
