package com.jungsan.backend.service;

import com.jungsan.backend.dto.GameDto;
import com.jungsan.backend.dto.ParticipantDto;
import com.jungsan.backend.entity.Game;
import com.jungsan.backend.entity.GameParticipant;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.exception.BusinessException;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.mapper.GameMapper;
import com.jungsan.backend.repository.GameParticipantRepository;
import com.jungsan.backend.repository.GameRepository;
import com.jungsan.backend.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;
    private final GameParticipantRepository gameParticipantRepository;
    private final ParticipantRepository participantRepository;
    private final GameMapper gameMapper;

    private static final int MIN_PARTICIPANTS = 2;
    private static final int MAX_PARTICIPANTS = 10;

    public List<GameDto.Response> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return gameMapper.toResponseList(games);
    }

    public List<GameDto.Response> getGamesByStatus(Game.GameStatus status) {
        List<Game> games = gameRepository.findByStatusOrderByCreatedAtDesc(status);
        return gameMapper.toResponseList(games);
    }

    public GameDto.Response getGameById(UUID id) {
        Game game = gameRepository.findByIdWithParticipants(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", id));
        return gameMapper.toResponse(game);
    }

    @Transactional
    public GameDto.Response createGame(GameDto.Create dto) {
        Game game = gameMapper.toEntity(dto);
        Game savedGame = gameRepository.save(game);
        return gameMapper.toResponse(savedGame);
    }

    @Transactional
    public GameDto.Response updateGame(UUID id, GameDto.Update dto) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", id));
        
        gameMapper.updateEntity(dto, game);
        
        if (dto.getStatus() == Game.GameStatus.COMPLETED && game.getCompletedAt() == null) {
            game.setCompletedAt(LocalDateTime.now());
        }
        
        Game savedGame = gameRepository.save(game);
        return gameMapper.toResponse(savedGame);
    }

    @Transactional
    public void deleteGame(UUID id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", id));
        
        gameRepository.delete(game);
    }

    @Transactional
    public GameDto.Response addParticipant(UUID gameId, GameDto.AddParticipant dto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        
        Participant participant = participantRepository.findByIdAndIsActiveTrue(dto.getParticipantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getParticipantId()));
        
        if (gameParticipantRepository.existsByGameIdAndParticipantId(gameId, dto.getParticipantId())) {
            throw new BusinessException("DUPLICATE_PARTICIPANT", "이미 참가한 참가자입니다");
        }
        
        long currentParticipantCount = gameParticipantRepository.countByGameId(gameId);
        if (currentParticipantCount >= MAX_PARTICIPANTS) {
            throw new BusinessException("MAX_PARTICIPANTS_EXCEEDED", "최대 참가자 수를 초과했습니다");
        }
        
        GameParticipant gameParticipant = GameParticipant.builder()
                .game(game)
                .participant(participant)
                .build();
        
        gameParticipantRepository.save(gameParticipant);
        
        Game updatedGame = gameRepository.findByIdWithParticipants(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        return gameMapper.toResponse(updatedGame);
    }

    @Transactional
    public GameDto.Response removeParticipant(UUID gameId, UUID participantId) {
        GameParticipant gameParticipant = gameParticipantRepository.findByGameIdAndParticipantId(gameId, participantId)
                .orElseThrow(() -> new ResourceNotFoundException("GameParticipant", "gameId and participantId", gameId + ", " + participantId));
        
        long currentParticipantCount = gameParticipantRepository.countByGameId(gameId);
        if (currentParticipantCount <= MIN_PARTICIPANTS) {
            throw new BusinessException("MIN_PARTICIPANTS_REQUIRED", "최소 참가자 수는 " + MIN_PARTICIPANTS + "명입니다");
        }
        
        gameParticipantRepository.delete(gameParticipant);
        
        Game updatedGame = gameRepository.findByIdWithParticipants(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        return gameMapper.toResponse(updatedGame);
    }

    public List<ParticipantDto.Simple> getGameParticipants(UUID gameId) {
        List<GameParticipant> gameParticipants = gameParticipantRepository.findByGameIdWithParticipant(gameId);
        return gameParticipants.stream()
                .map(gp -> ParticipantDto.Simple.builder()
                        .id(gp.getParticipant().getId())
                        .name(gp.getParticipant().getName())
                        .avatar(gp.getParticipant().getAvatar())
                        .build())
                .toList();
    }
}
