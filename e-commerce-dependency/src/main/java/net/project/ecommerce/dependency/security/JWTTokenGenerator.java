package net.project.ecommerce.dependency.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import net.project.ecommerce.dependency.configuration.SecurityTokenKeysConfig;
import net.project.ecommerce.dependency.constants.GeneralConstants;
import net.project.ecommerce.dependency.dto.request.TokenRequestDTO;
import net.project.ecommerce.dependency.dto.response.TokenResponseDTO;
import net.project.ecommerce.dependency.interfaces.ITokenGenerate;
import net.project.ecommerce.dependency.util.CipherUtil;

@Service(GeneralConstants.JWT_SERVICE)
public class JWTTokenGenerator implements ITokenGenerate{
	
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SecurityTokenKeysConfig config;
	
	@Autowired
	private CipherUtil cipher;		
	
	private Algorithm algorithm;
	
	/**
	 * Get the token key generator
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * 
	 * @return {@link String}
	 */
	private String obCode() {		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			byte[] fir = digest.digest(config.getTokenKey().getBytes(StandardCharsets.UTF_8));
			return new String(Base64.getEncoder().encode(fir),
					StandardCharsets.UTF_8);
		} catch (Exception e) {
			return config.getTokenKey();
		}
	}

	/**
	 * Metodo sirve para generar token
	 * 
	 * @author Allan Suarez <mailto:arsuarez@telconet.ec>
	 * @version 1.0
	 * @since 03/09/204
	 * 	
	 */
	@Override
	public TokenResponseDTO generateToken(TokenRequestDTO token) throws Exception {

		if ((token == null) 
	     || (token.getIssuer() == null)
		 || (token.getSubject() == null)
		 || (token.getCreationUser() == null)) 
		{
			log.error("The data required to generate the token is missing.");
			throw new IllegalStateException("The data required to generate the token is missing.");			
		}
		
		Long minutes = config.getExpirationTime();
		
		if(token.getTimeExpToken() != null)
		{
			minutes = token.getTimeExpToken();
		}			
	
		log.info("Issuer: {}, Subject: {}, CreationUser: {}", token.getIssuer(), token.getSubject(), token.getCreationUser());

		algorithm = Algorithm.HMAC256(Base64.getDecoder().decode(obCode()));
				
		String jwt = JWT.create()
					    .withSubject(token.getSubject())					    					   
			            .withIssuedAt(new Date(System.currentTimeMillis()))
			            .withExpiresAt(new Date(System.currentTimeMillis() + (minutes * 60 * 1000)))
			            .withClaim("roles", token.getClaims())
			            .withIssuer(token.getIssuer()).sign(algorithm);		
		
		String encryptToken = cipher.encrypt(jwt);
		
		log.info("Generated token : {} , for user {{}}",encryptToken,token.getSubject());
		
		return TokenResponseDTO.builder().token(encryptToken).build();
	}

	/**
	 * Validate token method
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * @param tokenEncr {@link String}	
	 */
	@Override
	public void validateToken(String tokenEncr) throws Exception 
	{
		algorithm               = Algorithm.HMAC256(Base64.getDecoder().decode(obCode()));
		JWTVerifier verificador = JWT.require(algorithm).build();
		String decodificado     = cipher.decrypt(tokenEncr);
		verificador.verify(decodificado);	
	}

	/**
	 * Get subject from jwt
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * 
	 * @param token {@link String}	
	 * @return {@link String}
	 */
	@Override
	public String getSubject(String token) {
		return JWT.decode(token).getSubject();		
	}
	
	/**
	 * Get Issuser from jwt
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * @param token {@link String}	
	 * @return {@link String}
	 */
	@Override
	public String getIssuer(String token) {
		return JWT.decode(token).getIssuer();		
	}
	
	/**
	 * Get claim from jwt
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * @param token {@link String}
	 * @param nombreClaim {@link String}	
	 * @return {@link String}
	 */
	@Override
	public Claim getClaim(String token,String claimName)  throws Exception {
		return JWT.decode(token).getClaim(claimName);	
	}

	/**
	 * Decode token from cypher encrypt
	 * 
	 * @author Allan Suarez <mailto:arsc86@gmail.com>
	 * @param tokenEncr {@link String}
	 * @return {@link String}
	 */
	@Override
	public String decodeCypherToken(String tokenEncr) throws Exception {				
		return cipher.decrypt(tokenEncr); 		
	}
}
