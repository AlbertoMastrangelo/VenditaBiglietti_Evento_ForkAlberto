package it.dedagroup.venditabiglietti.repository;

import it.dedagroup.venditabiglietti.dto.request.FiltraEventoDTORequest;
import it.dedagroup.venditabiglietti.model.Evento;
import it.dedagroup.venditabiglietti.service.def.EventoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CriteriaEventoRepository {
    @Autowired
    private EntityManager manager;

    public List<Evento> filtraEventi(FiltraEventoDTORequest request) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Evento> root = query.from(Evento.class);
        List<Predicate> predicate = new ArrayList<>();
        if (request.getData() != null) {
            predicate.add(builder.greaterThanOrEqualTo(root.get("data"), request.getData()));
        }
        if(request.getDescrizione() != null) predicate.add(builder.like(builder.lower(root.get("descrizione")), "%"+ request.getDescrizione().toLowerCase()+"%"));
        Predicate[] predicateArray = predicate.toArray(new Predicate[predicate.size()]);
        query.where(predicateArray);
        List<Tuple> list = manager.createQuery(query).getResultList();
        return list.stream().map(t -> t.get(0, Evento.class))
                .sorted(Comparator.comparing(Evento::getData).thenComparing(Evento::getOra))
                .toList();
    }
}
