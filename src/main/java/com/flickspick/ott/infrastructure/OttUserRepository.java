package com.flickspick.ott.infrastructure;

import com.flickspick.ott.domain.OttUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OttUserRepository extends JpaRepository<OttUser, Long> {
    @Transactional(readOnly = true)
    Boolean existsByUidAndOttId(Long uid, Long ottId);

    @Transactional(readOnly = true)
    List<OttUser> findAllByUid(Long uid);
}
