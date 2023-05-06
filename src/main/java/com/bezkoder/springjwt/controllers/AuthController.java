package com.bezkoder.springjwt.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin({"http://localhost:9001/demande","http://localhost:3000","http://localhost:8081","http://localhost:8081/users"})

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    System.out.println(  userDetails.getImage());
    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(),
                         userDetails.getImage(),roles,
                         userDetails.getNom(),userDetails.getPrenom(), userDetails.getSexe(), userDetails.getTelephone()
                         ));

  }

  //@GetMapping("/userr")
 // public Long getUserId() {
 //   UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 //   Long userId = userDetails.getId();
 //   return userId;
 // }
  @GetMapping("/userrr")
  public List<User> find(){
    List<User> u = userRepository.findAll();
    return u;
  }
  @GetMapping("/user/{id}")
  public User findById(@PathVariable int id){
    User u = userRepository.findById(id);
    return u;
  }
  @Transactional
  @PutMapping("/updateEtat/{id}")
  public void UpdateEtat(@PathVariable int id){
    User u = userRepository.findById(id);
    if(u.isEtat()){
      u.setEtat(false);
System.out.println("false");
    }else
    {
      u.setEtat(true);
      System.out.println("true");
    }
  }


  @Transactional
  @PutMapping("/updateSolde/{id}")
  public void UpdateSolde(@PathVariable int id){
    User u = userRepository.findById(id);
    if(u.getSolde()==0){
      u.setSolde(22);
    }else
    {
      u.setSolde(u.getSolde());
    }
  }











  @Transactional
  @PutMapping("/true/{id}")
  public void UpdateEtatTrue(@PathVariable int id){
    User u = userRepository.findById(id);
      u.setEtat(true);
      System.out.println("true");

  }
  @Transactional
  @PutMapping("/false/{id}")
  public void UpdateEtatFalse(@PathVariable int id){
    User u = userRepository.findById(id);
    u.setEtat(false);
    System.out.println("false");

  }












  @Transactional
  @DeleteMapping("/delete/{id}")
  public void delete(@PathVariable int id) {


        userRepository.deleteById(id);
        System.out.println("User " + id + " deleted successfully");


  }


  @GetMapping ("/solde/{id}")
  int getSolde(@PathVariable int id){
    return userRepository.getSolde(id);
  }
  @PutMapping ("/solde/{n}/{id}")
  void updateSolde(@PathVariable int n,@PathVariable int id){
    userRepository.updateSolde(n,id);
  }

 @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }
     User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),
            signUpRequest.getImage(),22,false,
            signUpRequest.getNom(),
            signUpRequest.getPrenom(),
             signUpRequest.getSexe(), signUpRequest.getTelephone()
            );


    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }







  @PutMapping("/update/{id}")

  public ResponseEntity<?> updateUser( @PathVariable("id") int userId, @Valid @RequestBody SignupRequest signUpRequest) {
    User user = userRepository.findById(userId);

    if (!user.getUsername().equals(signUpRequest.getUsername()) && userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (!user.getEmail().equals(signUpRequest.getEmail()) && userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    user.setUsername(signUpRequest.getUsername());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    user.setSolde(signUpRequest.getSolde());
user.setNom(signUpRequest.getNom());
user.setPrenom(signUpRequest.getPrenom());
user.setSexe(signUpRequest.getSexe());
   user.setImage(signUpRequest.getImage());

    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
  }

}









