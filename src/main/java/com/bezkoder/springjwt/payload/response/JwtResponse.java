package com.bezkoder.springjwt.payload.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private int id;
  private String username;
  private String email;
  private String image;
  private String nom;
  private String prenom;
  private String sexe;
  private int telephone;
  private List<String> roles;

  public String getImage() {
    return image;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getSexe() {
    return sexe;
  }

  public void setSexe(String sexe) {
    this.sexe = sexe;
  }

  public int getTelephone() {
    return telephone;
  }

  public void setTelephone(int telephone) {
    this.telephone = telephone;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public JwtResponse(String accessToken, int id, String username, String email, String image, List<String> roles,String nom,String prenom,String sexe,int telephone ) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.image=image;
    this.roles = roles;
    this.nom=nom;
    this.prenom=prenom;
    this.sexe=sexe;
    this.telephone=telephone;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }
}
