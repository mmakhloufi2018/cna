package ma.cdgp.af.esgaf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InstanceConfig {

    private final int instanceId;

    public InstanceConfig(@Value("${instance.id:0}") int instanceId) {
        this.instanceId = instanceId;
    }

    public int getInstanceId() {
        return instanceId;
    }
}
