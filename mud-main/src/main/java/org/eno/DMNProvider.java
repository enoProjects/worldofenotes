package org.eno;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;

public class DMNProvider {



    public DMNProvider() {
        KieServices kieServices = KieServices.Factory.get();
        ReleaseId releaseId = kieServices
                .newReleaseId("org.eno", "CommandTokenizer", "1.0-SNAPSHOT");
        KieContainer kContainer = kieServices.newKieContainer(releaseId);
        KieScanner kScanner = kieServices.newKieScanner(kContainer);

        // Start KIE scanner for polling the Maven repository every 10 seconds (10000 ms)
        kScanner.start(10000L);
        
        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "http://www.redhat.com/_c7328033-c355-43cd-b616-0aceef80e52a"; // TODO check the file for correct one
        String modelName = "Tokenizer";

        DMNModel dmnModel = dmnRuntime.getModel(namespace, modelName);


    }
}
