package br.edu.ifsp.matheus.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class User {
	private Long id;
	private String name;
	private String email;
	private String password;

	public User() {
	}
	
	public User(String name, String email, String password) {
		this(null, name, email, password, false);
	}

	public User(Long id, String name, String email, String password, boolean fromDB) {
		if (id != null) {
			this.id = id;
		}
		this.name = name;
		this.email = email;
		this.password = password;

		if (fromDB) {
			this.password = password;
		} else {
			this.password = hashSHA256(password);
		}
	}

	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static boolean authenticate(User user, String email, String password) {
		if (user != null) {
			return hashSHA256(password).equals(user.getPassword()) && email.equals(user.getEmail());
		}
		return false;
	}

	private static String hashSHA256(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro ao criptografar");
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, name, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password);
	}
}
