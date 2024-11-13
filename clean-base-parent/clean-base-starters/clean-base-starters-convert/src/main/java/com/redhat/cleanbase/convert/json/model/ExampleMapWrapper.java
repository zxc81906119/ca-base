package com.redhat.cleanbase.convert.json.model;

import com.redhat.cleanbase.convert.json.annotation.MapPathProperty;
import com.redhat.cleanbase.convert.json.model.base.BaseMapWrapper;
import com.redhat.cleanbase.convert.json.util.DelegateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;

/**
 * @author ming
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExampleMapWrapper implements BaseMapWrapper {

    @MapPathProperty("a.b.c[0].d[0][1]")
    private String field;

    public static void main(String[] args) {
        val mapWrapper = DelegateUtil.getJsonModelProxy(ExampleMapWrapper.class);
        mapWrapper.setField("haha");
        System.out.println(mapWrapper.unwrap());
    }


}
