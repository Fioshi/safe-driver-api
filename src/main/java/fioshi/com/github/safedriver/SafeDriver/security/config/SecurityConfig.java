package fioshi.com.github.safedriver.SafeDriver.security.config;

// <<< GARANTA QUE TODOS OS IMPORTS NECESSÁRIOS ESTÃO PRESENTES
import fioshi.com.github.safedriver.SafeDriver.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // <<< PARTE ESSENCIAL 1: SEU FILTRO JWT (restaurado do código antigo)
    // Precisamos de um bean para o seu filtro para poder usá-lo na cadeia de segurança.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita a configuração de CORS que definimos no bean abaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Desabilita o CSRF para APIs stateless
                .csrf(csrf -> csrf.disable())

                // Define a política de sessão como STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as regras de autorização para as rotas
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público às rotas de autenticação e documentação
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // <<< CORREÇÃO DO ERRO 403 (restaurada da nossa correção anterior)
                        // Permite acesso à rota de dados do usuário apenas se estiver autenticado
                        .requestMatchers("/api/v1/users/loged").authenticated()

                        // Exige autenticação para qualquer outra rota não especificada
                        .anyRequest().authenticated()
                );

        // <<< PARTE ESSENCIAL 2: ADICIONA SEU FILTRO JWT À CADEIA DE SEGURANÇA (restaurado)
        // Isso garante que o token seja validado em cada requisição protegida.
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // <<< MELHORIA: Usando seu novo padrão de CORS mais seguro (mantido)
        // Permite qualquer porta na origem localhost, perfeito para desenvolvimento.
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        // Permite que o frontend acesse credenciais (como cookies) se necessário
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // <<< MELHORIA: Usando seu novo bean AuthenticationManager (mantido)
    // Esta é a forma moderna de expor o AuthenticationManager como um bean.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}