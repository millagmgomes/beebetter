package neurocode.beebetter.controller;

import neurocode.beebetter.dto.RewardResponseDTO;
import neurocode.beebetter.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/mascot/{mascotId}")
    public ResponseEntity<List<RewardResponseDTO>> listByMascot(@PathVariable Long mascotId) {
        return ResponseEntity.ok(rewardService.listByMascot(mascotId));
    }
}
