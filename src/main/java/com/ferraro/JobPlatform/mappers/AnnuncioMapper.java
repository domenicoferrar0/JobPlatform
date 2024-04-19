package com.ferraro.JobPlatform.mappers;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.AnnuncioDTOSimple;
import com.ferraro.JobPlatform.dto.request.AnnuncioRequest;
import com.ferraro.JobPlatform.model.document.Annuncio;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

@Mapper(componentModel = "Spring")
public interface AnnuncioMapper {
    AnnuncioMapper INSTANCE = Mappers.getMapper(AnnuncioMapper.class);

    public Annuncio requestToAnnuncio(AnnuncioRequest request);

    public AnnuncioDTO annuncioToDTO(Annuncio annuncio);

    public AnnuncioDTOSimple annuncioToDTOSimple(Annuncio annuncio);

    default Annuncio updateAnnuncio(Annuncio annuncio, AnnuncioRequest request){
        annuncio.setTitle( request.getTitle() );
        annuncio.setDescription( request.getDescription() );
        annuncio.setLocalita( request.getLocalita() );
        annuncio.setModalita( request.getModalita() );
        annuncio.setDisponibilita( request.getDisponibilita() );
        annuncio.setCountry( request.getCountry() );
        annuncio.setUpdateDate( LocalDate.now());
        return annuncio;
    }
}
