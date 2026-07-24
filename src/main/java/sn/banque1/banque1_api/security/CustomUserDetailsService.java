package sn.banque1.banque1_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.banque1.banque1_api.repository.CompteRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CompteRepository compteRepository;

    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        return compteRepository.findByTelephone(telephone)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Compte non trouvé"));
    }

    private UserDetails toUserDetails(sn.banque1.banque1_api.model.Compte compte) {
        return User.builder()
                .username(compte.getTelephone())
                .password(compte.getPin())
                .build();
    }
}