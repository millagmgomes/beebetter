package neurocode.beebetter.service;

import neurocode.beebetter.dto.AlarmRequestDTO;
import neurocode.beebetter.dto.AlarmResponseDTO;
import neurocode.beebetter.model.Alarm;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import neurocode.beebetter.repository.AlarmRepository;

import java.util.List;

@Service
public class AlarmService {

    @Autowired private AlarmRepository alarmRepository;
    @Autowired
    private UserRepository userRepository;

    public AlarmResponseDTO create(Long userId, AlarmRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Alarm alarm = Alarm.builder()
                .time(dto.time())
                .label(dto.label())
                .ringtone(dto.ringtone())
                .active(true)
                .user(user)
                .build();

        alarmRepository.save(alarm);
        return toDTO(alarm);
    }

    public List<AlarmResponseDTO> listByUser(Long userId) {
        return alarmRepository.findByUserId(userId)
                .stream().map(this::toDTO).toList();
    }

    public AlarmResponseDTO toggleActive(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("Alarme não encontrado"));
        alarm.setActive(!alarm.isActive());
        alarmRepository.save(alarm);
        return toDTO(alarm);
    }

    public void delete(Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    private AlarmResponseDTO toDTO(Alarm a) {
        return new AlarmResponseDTO(
                a.getId(), a.getTime(), a.getLabel(), a.isActive(), a.getRingtone()
        );
    }

    public AlarmResponseDTO update(Long alarmId, AlarmRequestDTO dto) {
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("Alarme não encontrado"));
        alarm.setTime(dto.time());
        alarm.setLabel(dto.label());
        alarm.setRingtone(dto.ringtone());
        alarmRepository.save(alarm);
        return toDTO(alarm);
    }
}