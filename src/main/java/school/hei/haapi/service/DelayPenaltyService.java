package school.hei.haapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.DelayPenalty;
import school.hei.haapi.repository.DelayPenaltyRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DelayPenaltyService {
    public DelayPenaltyRepository delayPenaltyRepository;


    public DelayPenalty getMostRecentDelayPenalty() {
        List<DelayPenalty> delayPenalties = delayPenaltyRepository.findMostRecent(PageRequest.of(0,1));
        return delayPenalties.get(0);
    }
}
