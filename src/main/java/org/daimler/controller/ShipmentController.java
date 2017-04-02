package org.daimler.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.daimler.entity.order.Shipment;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.UnknownResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @RequestMapping(method = RequestMethod.GET, path = "/{trackingId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Shipment getShipmentDetailsByTrackingId(@RequestParam("trackingId") Long trackingId) {

        //TODO: implement the logic
        return new Shipment();
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Shipment shipment) throws EntityPersistenceException {
        //TODO: implement the logic
    }

    //TODO: Swagger Issue: check this link: https://github.com/springfox/springfox/issues/1638
    @RequestMapping(method = RequestMethod.POST, path = "/bulk")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public Shipment bulkCreate(@RequestBody Set<Shipment> shipments) throws EntityPersistenceException {
        //TODO: create the shipment entity and return the new ones
        return new Shipment();
    }

    @RequestMapping(method = RequestMethod.PUT, path = "{shipmentId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Shipment update(@PathVariable("shipmentId") Long orderId, @RequestBody Shipment shipment) throws EntityPersistenceException {
        //TODO: update the order entity and return the updated one
        return new Shipment();
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{shipmentId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Shipment patch(@PathVariable("shipmentId") String shipmentId, @RequestBody JsonPatch jsonPatch) throws UnknownResourceException, EntityPersistenceException {
        //TODO: update the shipment entity and return the updated one
        return new Shipment();
    }
}
