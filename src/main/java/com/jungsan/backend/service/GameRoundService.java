package com.jungsan.backend.service;

import com.jungsan.backend.dto.GameRoundDto;
import com.jungsan.backend.entity.Game;
import com.jungsan.backend.entity.GameRound;
import com.jungsan.backend.entity.Participant;
import com.jungsan.backend.exception.BusinessException;
import com.jungsan.backend.exception.ResourceNotFoundException;
import com.jungsan.backend.mapper.GameRoundMapper;
import com.jungsan.backend.repository.GameRepository;
import com.jungsan.backend.repository.GameRoundRepository;
import com.jungsan.backend.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameRoundService {

    private final GameRoundRepository gameRoundRepository;
    private final GameRepository gameRepository;
    private final ParticipantRepository participantRepository;
    private final GameRoundMapper gameRoundMapper;

    public List<GameRoundDto.Response> getGameRounds(UUID gameId) {
        List<GameRound> rounds = gameRoundRepository.findByGameIdWithPayments(gameId);
        return gameRoundMapper.toResponseList(rounds);
    }

    public GameRoundDto.Response getGameRoundById(UUID gameId, UUID roundId) {
        GameRound round = gameRoundRepository.findByIdWithPayments(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("GameRound", "id", roundId));
        
        if (!round.getGame().getId().equals(gameId)) {
            throw new BusinessException("INVALID_GAME_ROUND", "해당 게임의 라운드가 아닙니다");
        }
        
        return gameRoundMapper.toResponse(round);
    }

    @Transactional
    public GameRoundDto.Response createGameRound(UUID gameId, GameRoundDto.Create dto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game", "id", gameId));
        
        Participant winner = participantRepository.findByIdAndIsActiveTrue(dto.getWinnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getWinnerId()));
        
        // 라운드 번호 중복 확인
        if (gameRoundRepository.existsByGameIdAndRoundNumber(gameId, dto.getRoundNumber())) {
            throw new BusinessException("DUPLICATE_ROUND_NUMBER", "이미 존재하는 라운드 번호입니다");
        }
        
        GameRound round = GameRound.builder()
                .game(game)
                .roundNumber(dto.getRoundNumber())
                .winner(winner)
                .build();
        
        GameRound savedRound = gameRoundRepository.save(round);
        return gameRoundMapper.toResponse(savedRound);
    }

    @Transactional
    public GameRoundDto.Response updateGameRound(UUID gameId, UUID roundId, GameRoundDto.Update dto) {
        GameRound round = gameRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("GameRound", "id", roundId));
        
        if (!round.getGame().getId().equals(gameId)) {
            throw new BusinessException("INVALID_GAME_ROUND", "해당 게임의 라운드가 아닙니다");
        }
        
        Participant winner = participantRepository.findByIdAndIsActiveTrue(dto.getWinnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", dto.getWinnerId()));
        
        round.setWinner(winner);
        GameRound savedRound = gameRoundRepository.save(round);
        return gameRoundMapper.toResponse(savedRound);
    }

    @Transactional
    public void deleteGameRound(UUID gameId, UUID roundId) {
        GameRound round = gameRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("GameRound", "id", roundId));
        
        if (!round.getGame().getId().equals(gameId)) {
            throw new BusinessException("INVALID_GAME_ROUND", "해당 게임의 라운드가 아닙니다");
        }
        
        gameRoundRepository.delete(round);
    }

    public Integer getNextRoundNumber(UUID gameId) {
        return gameRoundRepository.findMaxRoundNumberByGameId(gameId)
                .map(max -> max + 1)
                .orElse(1);
    }
}
