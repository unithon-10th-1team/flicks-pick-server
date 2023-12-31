package com.flickspick.home.application;

import com.flickspick.auth.model.AuthUser;
import com.flickspick.exception.dto.ErrorType;
import com.flickspick.exception.user.UserNotFoundException;
import com.flickspick.home.dto.response.MyHomeResponse;
import com.flickspick.home.dto.response.MyProfileResponse;
import com.flickspick.movie.application.MovieService;
import com.flickspick.movie.model.MovieModel;
import com.flickspick.ott.model.OttModel;
import com.flickspick.ott.service.OttService;
import com.flickspick.recommendtype.application.RecommendTypeService;
import com.flickspick.recommendtype.model.RecTypeModel;
import com.flickspick.user.application.UserService;
import com.flickspick.user.infrastructure.UserRepository;
import com.flickspick.user.model.UserModel;
import com.flickspick.user_movie_history.application.UserMovieHistoryService;
import com.flickspick.user_movie_history.domain.UserMovieHistory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final UserMovieHistoryService userMovieHistoryService;
    private final RecommendTypeService recommendTypeService;
    private final MovieService movieService;
    private final OttService ottService;
    private final UserService userService;

    public MyHomeResponse getHome(AuthUser authUser) {
        var user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(
                                () -> new UserNotFoundException(ErrorType.USER_NOT_FOUND_ERROR));
        UserModel userModel = UserModel.from(user);
        UserMovieHistory userMovieHistory = userMovieHistoryService.getRecentHistory(user.getId());
        RecTypeModel recTypeModel =
                recommendTypeService.getRecTypeModel(userMovieHistory.getRecommendTypeId());

        List<String> tags = recTypeModel.getTags();

        List<MovieModel> similarMovies = movieService.getList(userMovieHistory.getMovieId(), 2);
        MovieModel movieModel = movieService.get(userMovieHistory.getMovieId());
        similarMovies.add(movieModel);

        List<MovieModel> differentMovies = movieService.getList(userMovieHistory.getMovieId(), 3);

        return MyHomeResponse.toResponse(
                userModel, List.of(recTypeModel), tags, similarMovies, differentMovies);
    }

    public MyProfileResponse getMyProfile(AuthUser authUser) {
        var userModel = userService.getUserModel(authUser.getId());

        UserMovieHistory userMovieHistory =
                userMovieHistoryService.getRecentHistory(userModel.getId());
        RecTypeModel recTypeModel = recommendTypeService.get(userMovieHistory.getRecommendTypeId());

        List<String> tags = recTypeModel.getTags();

        List<MovieModel> similarMovies = movieService.getList(userMovieHistory.getMovieId(), 2);
        MovieModel movieModel = movieService.get(userMovieHistory.getMovieId());
        similarMovies.add(movieModel);

        List<OttModel> ottModelList =
                ottService.findAllByUid(userModel.getId()).stream()
                        .map(ottUser -> ottService.get(ottUser.getOttId()))
                        .collect(Collectors.toList());
        return MyProfileResponse.toResponse(
                userModel, ottModelList, List.of(recTypeModel), tags, similarMovies);
    }
}
