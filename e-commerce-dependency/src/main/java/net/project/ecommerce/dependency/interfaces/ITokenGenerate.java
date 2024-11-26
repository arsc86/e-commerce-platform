package net.project.ecommerce.dependency.interfaces;

import net.project.ecommerce.dependency.dto.request.TokenRequestDTO;
import net.project.ecommerce.dependency.dto.response.TokenResponseDTO;

public interface ITokenGenerate {
	
	public TokenResponseDTO generateToken(TokenRequestDTO token) throws Exception; 
	public void validateToken(String token) throws Exception ;	
	public String decodeCypherToken(String token) throws Exception;
	public String getSubject(String token);
	public String getIssuer(String token);
	public Object getClaim(String token, String nombreClaim) throws Exception;	

}
