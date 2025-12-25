package com.resume.resumematching.upload;

import com.resume.resumematching.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parse")
@RequiredArgsConstructor
public class ParsingController {

    private final ParsingService parsingService;

    @PostMapping("/{uploadId}")
    public ResponseEntity<ApiResponse<Void>> parse(
            @PathVariable Long uploadId
    ) {

        parsingService.parseUpload(uploadId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Parsing completed successfully",
                        null
                )
        );
    }
}
