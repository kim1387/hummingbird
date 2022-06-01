package com.hummingbird.backend.owner.controller;

import com.hummingbird.backend.owner.domain.Owner;
import com.hummingbird.backend.owner.dto.OwnerDto;
import com.hummingbird.backend.owner.dto.OwnerInfoDto;
import com.hummingbird.backend.owner.dto.OwnerLoginRequest;
import com.hummingbird.backend.owner.dto.OwnerProfileDto;
import com.hummingbird.backend.owner.service.OwnerProfileService;
import com.hummingbird.backend.owner.service.serviceImpl.GeneralOwnerService;
import com.hummingbird.backend.owner.util.OwnerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hummingbird.backend.owner.controller.OwnerController.OWNER_API_URI;


@RestController
@RequestMapping(OWNER_API_URI)
@RequiredArgsConstructor
public class OwnerController {

    public static final String OWNER_API_URI = "/api/owner";

    private final GeneralOwnerService generalOwnerService;
    private final OwnerProfileService ownerProfileService;
    private final PasswordEncoder passwordEncoder;

    private final OwnerUtil ownerUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid OwnerDto ownerDto, BindingResult bindingResult) {

        // 이메일 중복 관련 상태 코드 정리 https://www.notion.so/ce19f003/409-vs-422-dbcfc2dfa3fe492488b37740efdf35a5
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("이미 가입된 이메일입니다.");
        }

        boolean isDuplicatedCustomer = generalOwnerService.isDuplicatedCustomer(ownerDto, passwordEncoder);

        if (isDuplicatedCustomer) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("회원 탈퇴된 유저입니다 고객센터에 문의해주세요.");
        }
        generalOwnerService.signup(ownerDto,passwordEncoder);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공");
    }
    @DeleteMapping("/{ownerId}")
    public ResponseEntity<String> deleteOwnerById(@PathVariable Long ownerId) {
        generalOwnerService.deleteOwnerById(ownerId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    @GetMapping
    public ResponseEntity<List<OwnerInfoDto>> readOwnerInfoAll() {
        List<OwnerInfoDto> ownerInfoDtos = generalOwnerService.readOwnerInfoAll();
        return ResponseEntity.ok(ownerInfoDtos);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid OwnerLoginRequest ownerLoginRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        boolean isValidMember = generalOwnerService.isValidCustomer(ownerLoginRequest, passwordEncoder);

        if (isValidMember) {
            Long ownerId = generalOwnerService.findOwnerByEmail(ownerLoginRequest.getEmail()).getId();
            return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 실패");
    }

    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<OwnerProfileDto> getOwnerProfile() {
        return ResponseEntity.ok(ownerProfileService.getOwnerProfile());
    }

    @GetMapping("/admin/profile")
    public ResponseEntity<OwnerProfileDto> getAdminProfile() {
        Owner userEntityBySessionID = ownerUtil.getUserEntityBySessionID();

        if (!userEntityBySessionID.getBusinessRegistrationNumber().equals("ADMIN")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(ownerProfileService.getAdminProfile());
    }
}
