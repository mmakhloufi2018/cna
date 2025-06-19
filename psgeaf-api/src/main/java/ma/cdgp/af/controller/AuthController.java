package ma.cdgp.af.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ma.cdgp.af.oauth2.CustomAuthenticationToken;
import ma.cdgp.af.oauth2.JwtResponse;
import ma.cdgp.af.oauth2.JwtToken;
import ma.cdgp.af.oauth2.JwtUtils;
import ma.cdgp.af.oauth2.LoginRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Session")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/Create")
	@Operation(summary = "Service d'authentification")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Succès", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "404", description = "La syntaxe de la requête est erronée", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content) })
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().length() == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JwtResponse(false, "Mot de passe obligatoire"));
		}
		Authentication authentication = authenticationManager.authenticate(
				new CustomAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword(), null));

		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new JwtResponse(false, "Erreur interne"));
		}
		
		
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			List<String> jwtClient = jwtUtils.generateJwtToken(authentication);
			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(true, "Authentifié avec succès",
					new JwtToken(jwtClient.get(0), jwtClient.get(1), jwtClient.get(2))));
		} else
			return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(false, "Mot de passe incorrecte"));
	}

}
