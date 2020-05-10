package com.commerzbank.task.util;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Test task Commerzbank
 *
 * Catches exception when URI created and returns proper ResponseEntity
 *
 * @author vtanenya
 * */

public class RestUtil {
    public static ResponseEntity<?> getCreatedResponse(EntityModel<?> model, String errorMessage) {
        try {
            return ResponseEntity.created(new URI(model.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(model);
        }
        catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
