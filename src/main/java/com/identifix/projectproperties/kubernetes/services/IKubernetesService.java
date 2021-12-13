package com.identifix.projectproperties.kubernetes.services;

import com.identifix.projectproperties.kubernetes.models.IngressInfo;
import com.identifix.projectproperties.kubernetes.models.Namespace;
import com.identifix.projectproperties.kubernetes.models.Pod;
import com.identifix.projectproperties.kubernetes.models.Service;
import com.identifix.projectproperties.kubernetes.models.ServiceInfo;
import java.io.FileNotFoundException;
import java.util.List;

public interface IKubernetesService {

    void updateConfigFile(String absolutePath) throws FileNotFoundException;
    List<Service> getServices(String namespace, String config);
    List<Pod> getPods(String namespace);
    List<Namespace> getNamespaces(String config);
    IngressInfo getIngressInfo(String namespace, String service, String config);
    String getUrlForService(String namespace, String service, String config);
    ServiceInfo getServiceConfiguration(String namespace, String service, String config);
    String getServiceApplicationProperties(String namespace, String service, String config);

}
