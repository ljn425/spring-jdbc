package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
public class MemberServiceV3_3 {
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String formId, String toId, int money) throws SQLException {
        bizLogic(formId, toId, money);
    }

    private void bizLogic(String formId, String toId, int money) throws SQLException {
        Member formMember = memberRepository.findById(formId);
        Member toMember = memberRepository.findById(toId);

        //시작
        memberRepository.update(formId, formMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            log.info("이체중 예외");
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
