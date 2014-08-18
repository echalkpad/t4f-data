package io.datalayer.devops.jclouds;

import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.aws.ec2.AWSEC2ApiMetadata;
import org.jclouds.aws.ec2.AWSEC2Client;
import org.jclouds.aws.ec2.compute.AWSEC2TemplateOptions;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.domain.Location;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.inject.Module;

public class EC2Main {

    public static void main(String... args) throws RunNodesException {

        // Get a context with ec2 that offers the portable ComputeService API.
        ComputeServiceContext context = ContextBuilder. //
                newBuilder("aws-ec2"). //
                credentials("identity", "credential"). //
                modules(ImmutableSet.<Module> of(new Log4JLoggingModule(), new SshjSshClientModule())). //
                buildView(ComputeServiceContext.class);

        // Here's an example of the portable api
        Set<? extends Location> locations = context.getComputeService().listAssignableLocations();

        Set<? extends Image> images = context.getComputeService().listImages();

        // Pick the highest version of the RightScale CentOS template
        Template template = context.getComputeService().templateBuilder().osFamily(OsFamily.UBUNTU).build();

        // Specify your own groups which already have the correct rules applied
        template.getOptions().as(AWSEC2TemplateOptions.class).securityGroups("group1");

        // Specify your own keypair for use in creating nodes
        template.getOptions().as(AWSEC2TemplateOptions.class).keyPair("keyPair");

        // Run a couple nodes accessible via group
        Set<? extends NodeMetadata> nodes = context.getComputeService().createNodesInGroup("webserver", 2, template);

        // When you need access to very ec2-specific features, use the provider-specific context
        AWSEC2Client ec2Client = context.unwrap(AWSEC2ApiMetadata.CONTEXT_TOKEN).getApi();
        
        // Ex. to get an ip and associate it with a node
        NodeMetadata node = Iterables.get(nodes, 0);
        String ip = ec2Client.getElasticIPAddressServices().allocateAddressInRegion(node.getLocation().getId());
        ec2Client.getElasticIPAddressServices().associateAddressInRegion(node.getLocation().getId(), ip,
                node.getProviderId());

        context.close();

    }

}
