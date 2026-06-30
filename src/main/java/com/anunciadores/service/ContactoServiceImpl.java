package com.anunciadores.service;

import com.anunciadores.dto.ContactoDto;
import com.anunciadores.dto.ContactoRequestDto;
import com.anunciadores.model.Contacto;
import com.anunciadores.repository.IContactoRepo;
import com.anunciadores.service.interfaces.IContactoService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactoServiceImpl implements IContactoService {
    private final Logger log = LoggerFactory.getLogger(ContactoServiceImpl.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Autowired
    private IContactoRepo contactoRepo;

    @Override
    @Transactional
    public ContactoDto guardar(ContactoRequestDto request) {
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }
        if (!EMAIL_PATTERN.matcher(request.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del correo no es válido");
        }
        if (request.getAsunto() == null || request.getAsunto().trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto es requerido");
        }
        if (request.getMensaje() == null || request.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje es requerido");
        }

        Contacto contacto = new Contacto();
        contacto.setNombre(request.getNombre().trim());
        contacto.setEmail(request.getEmail().trim());
        contacto.setAsunto(request.getAsunto().trim());
        contacto.setMensaje(request.getMensaje().trim());
        contacto.setLeido(false);
        contacto.setFechaCreacion(new Date());

        Contacto guardado = contactoRepo.save(contacto);
        log.info("Nuevo contacto guardado: id={}, nombre={}, email={}", guardado.getId(), guardado.getNombre(), guardado.getEmail());
        return entityToDto(guardado);
    }

    @Override
    public List<ContactoDto> listarTodos() {
        return contactoRepo.findAllByOrderByFechaCreacionDesc()
            .stream()
            .map(this::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ContactoDto> listarNoLeidos() {
        return contactoRepo.findByLeidoFalseOrderByFechaCreacionDesc()
            .stream()
            .map(this::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactoDto marcarLeido(Integer id) {
        Optional<Contacto> optional = contactoRepo.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Contacto no encontrado con id: " + id);
        }
        Contacto contacto = optional.get();
        contacto.setLeido(true);
        Contacto actualizado = contactoRepo.save(contacto);
        return entityToDto(actualizado);
    }

    @Override
    public long contarNoLeidos() {
        return contactoRepo.countByLeidoFalse();
    }

    private ContactoDto entityToDto(Contacto entity) {
        return new ContactoDto(
            entity.getId(),
            entity.getNombre(),
            entity.getEmail(),
            entity.getAsunto(),
            entity.getMensaje(),
            entity.getLeido(),
            entity.getFechaCreacion()
        );
    }
}
