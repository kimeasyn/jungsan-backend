package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.GameRoundDto;
import com.jungsan.backend.entity.GameRound;
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
public class GameRoundMapperImpl implements GameRoundMapper {

    @Override
    public GameRound toEntity(GameRoundDto.Create dto) {
        if ( dto == null ) {
            return null;
        }

        GameRound gameRound = new GameRound();

        return gameRound;
    }

    @Override
    public void updateEntity(GameRoundDto.Update dto, GameRound entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public GameRoundDto.Response toResponse(GameRound entity) {
        if ( entity == null ) {
            return null;
        }

        GameRoundDto.Response response = new GameRoundDto.Response();

        return response;
    }

    @Override
    public GameRoundDto.Simple toSimple(GameRound entity) {
        if ( entity == null ) {
            return null;
        }

        GameRoundDto.Simple simple = new GameRoundDto.Simple();

        return simple;
    }

    @Override
    public List<GameRoundDto.Response> toResponseList(List<GameRound> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GameRoundDto.Response> list = new ArrayList<GameRoundDto.Response>( entities.size() );
        for ( GameRound gameRound : entities ) {
            list.add( toResponse( gameRound ) );
        }

        return list;
    }

    @Override
    public List<GameRoundDto.Simple> toSimpleList(List<GameRound> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GameRoundDto.Simple> list = new ArrayList<GameRoundDto.Simple>( entities.size() );
        for ( GameRound gameRound : entities ) {
            list.add( toSimple( gameRound ) );
        }

        return list;
    }
}
