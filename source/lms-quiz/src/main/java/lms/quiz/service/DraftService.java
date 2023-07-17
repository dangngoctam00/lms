package lms.quiz.service;

import lms.quiz.enums.DraftContext;
import lms.quiz.repository.DraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DraftService {

    private final DraftRepository draftRepository;

    public Optional<Integer> getRevisionNumber(DraftContext context, Long id) {
        return draftRepository.findDraftEntityByContextAndContextId(context, id)
                .map(x -> x.getRevisionNumber() - 1);
    }
}
