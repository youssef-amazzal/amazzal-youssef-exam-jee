package amazzal.youssef.examjee.services.impl;

import amazzal.youssef.examjee.dtos.CreditRequestDTO;
import amazzal.youssef.examjee.dtos.CreditResponseDTO;
import amazzal.youssef.examjee.dtos.RemboursementDTO;
import amazzal.youssef.examjee.entities.*;
import amazzal.youssef.examjee.mappers.CreditMapper;
import amazzal.youssef.examjee.mappers.RemboursementMapper;
import amazzal.youssef.examjee.repositories.ClientRepository;
import amazzal.youssef.examjee.repositories.CreditRepository;
import amazzal.youssef.examjee.repositories.RemboursementRepository;
import amazzal.youssef.examjee.services.CreditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final ClientRepository clientRepository;
    private final RemboursementRepository remboursementRepository;
    private final CreditMapper creditMapper;
    private final RemboursementMapper remboursementMapper;

    @Override
    public CreditResponseDTO demanderCredit(CreditRequestDTO requestDTO) {
        log.info("Demande de crédit reçue pour client ID: {}", requestDTO.getClientId());

        Client client = clientRepository.findById(requestDTO.getClientId()).orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + requestDTO.getClientId()));

        Credit credit;


        if (!StringUtils.hasText(requestDTO.getCreditType())) {
            throw new IllegalArgumentException("Credit type must be specified (PERSONNEL, IMMOBILIER, PROFESSIONNEL)");
        }

        switch (requestDTO.getCreditType().toUpperCase()) {
            case "PERSONNEL":
                CreditPersonnel cp = new CreditPersonnel();
                cp.setMotif(requestDTO.getMotif());
                credit = cp;
                break;
            case "IMMOBILIER":
                CreditImmobilier ci = new CreditImmobilier();
                ci.setTypeBienFinance(requestDTO.getTypeBienFinance());
                credit = ci;
                break;
            case "PROFESSIONNEL":
                CreditProfessionnel cpro = new CreditProfessionnel();
                cpro.setMotif(requestDTO.getMotif());
                cpro.setRaisonSocialeEntreprise(requestDTO.getRaisonSocialeEntreprise());
                credit = cpro;
                break;
            default:
                throw new IllegalArgumentException("Unknown credit type: " + requestDTO.getCreditType());
        }


        credit.setClient(client);
        credit.setMontant(requestDTO.getMontant());
        credit.setDureeRemboursement(requestDTO.getDureeRemboursement());
        credit.setDateDemande(new Date());
        credit.setStatut(StatutCredit.EN_COURS);


        credit.setTauxInteret(0.0);

        Credit savedCredit = creditRepository.save(credit);
        log.info("Crédit {} sauvegardé avec ID: {}", requestDTO.getCreditType(), savedCredit.getId());
        return creditMapper.toCreditResponseDTO(savedCredit);
    }

    @Override
    public CreditResponseDTO getCreditById(Long creditId) {
        log.info("Fetching credit with id: {}", creditId);
        Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new EntityNotFoundException("Credit with id " + creditId + " not found"));
        return creditMapper.toCreditResponseDTO(credit);
    }

    @Override
    public List<CreditResponseDTO> getCreditsByClientId(Long clientId) {
        log.info("Fetching credits for client id: {}", clientId);
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found, cannot fetch credits.");
        }
        List<Credit> credits = creditRepository.findByClientId(clientId);
        return creditMapper.toCreditResponseDTOs(credits);
    }

    @Override
    public List<CreditResponseDTO> getCreditsByStatut(StatutCredit statut) {
        log.info("Fetching credits with statut: {}", statut);
        List<Credit> credits = creditRepository.findByStatut(statut);
        return creditMapper.toCreditResponseDTOs(credits);
    }

    @Override
    public CreditResponseDTO approuverCredit(Long creditId) {
        log.info("Approving credit with id: {}", creditId);
        Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new EntityNotFoundException("Credit with id " + creditId + " not found"));
        if (credit.getStatut() != StatutCredit.EN_COURS) {
            throw new IllegalStateException("Credit cannot be approved. Current status: " + credit.getStatut());
        }
        credit.setStatut(StatutCredit.ACCEPTE);
        credit.setDateAcceptation(new Date());


        if (credit.getTauxInteret() == 0.0) {
            if (credit instanceof CreditPersonnel) credit.setTauxInteret(0.07);
            else if (credit instanceof CreditImmobilier) credit.setTauxInteret(0.035);
            else if (credit instanceof CreditProfessionnel) credit.setTauxInteret(0.05);
        }
        Credit savedCredit = creditRepository.save(credit);
        return creditMapper.toCreditResponseDTO(savedCredit);
    }

    @Override
    public CreditResponseDTO rejeterCredit(Long creditId) {
        log.info("Rejecting credit with id: {}", creditId);
        Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new EntityNotFoundException("Credit with id " + creditId + " not found"));
        if (credit.getStatut() != StatutCredit.EN_COURS) {
            throw new IllegalStateException("Credit cannot be rejected. Current status: " + credit.getStatut());
        }
        credit.setStatut(StatutCredit.REJETE);
        Credit savedCredit = creditRepository.save(credit);
        return creditMapper.toCreditResponseDTO(savedCredit);
    }

    @Override
    public RemboursementDTO ajouterRemboursement(Long creditId, RemboursementDTO remboursementDTO) {
        log.info("Adding remboursement to credit id: {}", creditId);
        Credit credit = creditRepository.findById(creditId).orElseThrow(() -> new EntityNotFoundException("Credit with id " + creditId + " not found"));

        if (credit.getStatut() != StatutCredit.ACCEPTE) {
            throw new IllegalStateException("Cannot add remboursement to a credit that is not accepted. Current status: " + credit.getStatut());
        }

        Remboursement remboursement = remboursementMapper.toRemboursement(remboursementDTO);
        remboursement.setCredit(credit);

        if (remboursement.getDateRemboursement() == null) {
            remboursement.setDateRemboursement(new Date());
        }

        Remboursement savedRemboursement = remboursementRepository.save(remboursement);
        return remboursementMapper.toRemboursementDTO(savedRemboursement);
    }

    @Override
    public List<RemboursementDTO> getRemboursementsByCreditId(Long creditId) {
        log.info("Fetching remboursements for credit id: {}", creditId);
        if (!creditRepository.existsById(creditId)) {
            throw new EntityNotFoundException("Credit with id " + creditId + " not found, cannot fetch remboursements.");
        }
        List<Remboursement> remboursements = remboursementRepository.findByCreditId(creditId);
        return remboursements.stream().map(remboursementMapper::toRemboursementDTO).collect(Collectors.toList());
    }
}