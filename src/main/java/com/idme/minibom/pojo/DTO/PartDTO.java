package com.idme.minibom.pojo.DTO;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class PartDTO {
    private Long id;
    private String modifier;
    private Timestamp last_update_time;
    private String creator;
    private Timestamp create_time;
    private String rdm_ext_type;
    private Long tenant_id;
    private String tenant_clazz;
    private String name;
    private String description;
    private String security_level;
    private String kiaguid;
    private boolean working_copy;
    private String checkout_username;
    private Timestamp checkout_time;
    private String part_enName;
    private String iteration_note;
    private String source;
    private boolean end_part;
    private String number;
    private boolean phantom_part;
}
