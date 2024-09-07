package hospital.com.controllers;

import hospital.com.adapter.HistoricoInternacoesAdapter;
import hospital.com.entities.HistoricoInternacoesEntity;
import hospital.com.entities.MedicosEntity;
import hospital.com.entities.PacientesEntity;
import hospital.com.models.HistoricoInternacoesPorMedico;
import hospital.com.models.HistoricoInternacoesPorPaciente;
import hospital.com.repositories.HistoricoInternacoesRepository;
import hospital.com.repositories.MedicosRepository;
import hospital.com.repositories.PacientesRepository;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico_internacoes")
public class HistoricoInternacoesController {
    @Autowired
    private final HistoricoInternacoesRepository historicoInternacoesRepository;
    @Autowired
    private final MedicosRepository medicosRepository;
    @Autowired
    private final PacientesRepository pacientesRepository;
    private HistoricoInternacoesAdapter historicoInternacoesAdapter = new HistoricoInternacoesAdapter();

    public HistoricoInternacoesController(HistoricoInternacoesRepository historicoInternacoesRepository, MedicosRepository medicosRepository, PacientesRepository pacientesRepository) {
        this.historicoInternacoesRepository = historicoInternacoesRepository;
        this.medicosRepository = medicosRepository;
        this.pacientesRepository = pacientesRepository;
    }
    @RolesAllowed("user")
    @GetMapping
    public List<HistoricoInternacoesEntity> findAll() {
        return this.historicoInternacoesRepository.findAll();
    }
    @RolesAllowed("user")
    @GetMapping("/{id}")
    public HistoricoInternacoesEntity findById(@PathVariable("id") final Long id) {
        return this.historicoInternacoesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id do histórico não encontrado!"));
    }
    @RolesAllowed("user")
    @GetMapping("/descricao/{descricao}")
    public List<HistoricoInternacoesEntity> findByDescricao(final String descricao) {
        return this.historicoInternacoesRepository.findByDescricao(descricao);
    }
    @RolesAllowed("user")
    @GetMapping("/por_medico")
    public List<HistoricoInternacoesPorMedico> findHistoricoInternacoesPorMedico() {
        List<MedicosEntity> medicos = medicosRepository.findAll();
        List<HistoricoInternacoesEntity> historicoInternacoes = historicoInternacoesRepository.findAll();
        List<HistoricoInternacoesPorMedico> historicoInternacoesPorMedicos = historicoInternacoesAdapter
                .historicoInternacoesPorMedicoAdapter(historicoInternacoes, medicos);
        return historicoInternacoesPorMedicos;
    }
    @RolesAllowed("user")
    @GetMapping("/por_paciente")
    public List<HistoricoInternacoesPorPaciente> findHistoricoInternacoesPorPaciente() {
        List<PacientesEntity> pacientes = pacientesRepository.findAll();
        List<HistoricoInternacoesEntity> historicoInternacoes = historicoInternacoesRepository.findAll();
        List<HistoricoInternacoesPorPaciente> historicoInternacoesPorPacientes = historicoInternacoesAdapter
                .historicoInternacoesPorPacienteAdapter(historicoInternacoes, pacientes);
        return historicoInternacoesPorPacientes;
    }
    @RolesAllowed("adm")
    @PostMapping("/adicionar")
    public void addHistorico(@RequestBody final HistoricoInternacoesEntity historico) {
        this.historicoInternacoesRepository.save(historico);
    }
    @RolesAllowed("adm")
    @PostMapping("/atualizar")
    public void update(@RequestBody final HistoricoInternacoesEntity historicoInternacoes) {
        this.historicoInternacoesRepository.save(historicoInternacoes);
    }
    @RolesAllowed("adm")
    @DeleteMapping("/excluir")
    public void deleteById(@PathVariable("id") final Long id) {
        this.historicoInternacoesRepository.deleteById(id);
    }
}