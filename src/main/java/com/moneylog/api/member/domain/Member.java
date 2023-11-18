package com.moneylog.api.member.domain;

import static com.moneylog.api.member.domain.Role.ROLE_ADMIN;
import static com.moneylog.api.member.domain.Role.ROLE_USER;

import com.moneylog.api.common.domain.BaseEntity;
import com.moneylog.api.member.dto.MemberCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import java.util.Objects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role = ROLE_USER;

    protected Member() {
    }

    @Builder
    private Member(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = Objects.isNull(role) ? this.role : role;
    }

    public static Member of(MemberCreateRequest memberCreateRequest, PasswordEncoder passwordEncoder) {
        String encodePassword = passwordEncoder.encode(memberCreateRequest.getPassword());
        return Member.builder()
                .email(memberCreateRequest.getEmail())
                .password(encodePassword)
                .role(memberCreateRequest.getRole())
                .build();
    }

    public boolean isAdmin() {
        return Objects.equals(this.role, ROLE_ADMIN);
    }
}
