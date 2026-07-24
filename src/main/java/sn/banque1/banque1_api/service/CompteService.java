package sn.banque1.banque1_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.banque1.banque1_api.model.Compte;
import sn.banque1.banque1_api.repository.CompteRepository;

@RequiredArgsConstructor
@Service
public class CompteService {

    private final CompteRepository compteRepository;

    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

    public List<Compte> findAllComptes() {
        return compteRepository.findAll();
    }

    public Optional<Compte> findByTelephone(String telephone) {
        return compteRepository.findByTelephone(telephone);
    }

    // public Boolean existByTelephone(String telephone) {
    // return compteRepository.existByTelephone(telephone);
    // }

    public void delete(Compte compte) {
        compteRepository.delete(compte);
    }

}
