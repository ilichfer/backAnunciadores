package com.anunciadores.service.interfaces;

import com.anunciadores.dto.ContactoDto;
import com.anunciadores.dto.ContactoRequestDto;
import java.util.List;

public interface IContactoService {

    ContactoDto guardar(ContactoRequestDto request);

    List<ContactoDto> listarTodos();

    List<ContactoDto> listarNoLeidos();

    ContactoDto marcarLeido(Integer id);

    long contarNoLeidos();
}
