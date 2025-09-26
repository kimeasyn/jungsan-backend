package com.jungsan.backend.mapper;

import com.jungsan.backend.dto.GameDto;
import com.jungsan.backend.entity.Game;
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
public class GameMapperImpl implements GameMapper {

    @Override
    public Game toEntity(GameDto.Create dto) {
        if ( dto == null ) {
            return null;
        }

        Game game = new Game();

        return game;
    }

    @Override
    public void updateEntity(GameDto.Update dto, Game entity) {
        if ( dto == null ) {
            return;
        }
    }

    @Override
    public GameDto.Response toResponse(Game entity) {
        if ( entity == null ) {
            return null;
        }

        GameDto.Response response = new GameDto.Response();

        return response;
    }

    @Override
    public GameDto.Simple toSimple(Game entity) {
        if ( entity == null ) {
            return null;
        }

        GameDto.Simple simple = new GameDto.Simple();

        return simple;
    }

    @Override
    public List<GameDto.Response> toResponseList(List<Game> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GameDto.Response> list = new ArrayList<GameDto.Response>( entities.size() );
        for ( Game game : entities ) {
            list.add( toResponse( game ) );
        }

        return list;
    }

    @Override
    public List<GameDto.Simple> toSimpleList(List<Game> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GameDto.Simple> list = new ArrayList<GameDto.Simple>( entities.size() );
        for ( Game game : entities ) {
            list.add( toSimple( game ) );
        }

        return list;
    }
}
