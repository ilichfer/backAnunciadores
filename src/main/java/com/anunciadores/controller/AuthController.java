package com.anunciadores.controller;
import com.anunciadores.auth.dto.LoginRequest;
import com.anunciadores.auth.dto.LoginResponse;
import com.anunciadores.service.AuthService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping({"/api/auth"})
@CrossOrigin(origins = {"*"})
public class AuthController {
@Autowired
private AuthService authService;
@PostMapping({"/loginReact"})
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
try {
LoginResponse response = this.authService.login(request);
return ResponseEntity.ok(response);
}
catch (RuntimeException e) {
return 
ResponseEntity.status(401)
.body(Map.of("message", e.getMessage()));
} 
}
}
