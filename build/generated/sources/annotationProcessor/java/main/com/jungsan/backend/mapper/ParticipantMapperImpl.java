package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Participant;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-26T13:10:18+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.16 (Homebrew)"
)
@Component
public class ParticipantMapperImpl implements ParticipantMapper {

    @Override
    public Participant toEntity(ParticipantDto.Create dto) {
        if ( dto == null ) {
            return null;
        }

        Participant participant = new Participant();

        return participant;
    }

    @Override
    public void updateEntity(ParticipantDto.Update dto, Participant entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public ParticipantDto.Response toResponse(Participant entity) {
        if ( entity == null ) {
            return null;
        }

        ParticipantDto.Response response = new ParticipantDto.Response();

        return response;
    }

    @Override
    public ParticipantDto.Simple toSimple(Participant entity) {
        if ( entity == null ) {
            return null;
        }

        ParticipantDto.Simple simple = new ParticipantDto.Simple();

        return simple;
    }

    @Override
    public List<ParticipantDto.Response> toResponseList(List<Participant> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ParticipantDto.Response> list = new ArrayList<ParticipantDto.Response>( entities.size() );
        for ( Participant participant : entities ) {
            list.add( toResponse( participant ) );
        }

        return list;
    }

    @Override
    public List<ParticipantDto.Simple> toSimpleList(List<Participant> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ParticipantDto.Simple> list = new ArrayList<ParticipantDto.Simple>( entities.size() );
        for ( Participant participant : entities ) {
            list.add( toSimple( participant ) );
        }

        return list;
    }
}
