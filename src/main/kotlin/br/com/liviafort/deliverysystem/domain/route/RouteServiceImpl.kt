package br.com.liviafort.deliverysystem.domain.route

import java.util.UUID
import org.springframework.stereotype.Service

@Service
class RouteServiceImpl(private val repository: RouteRepository) : RouteService {
    override fun create(route: Route) {
        repository.save(route)
    }

    override fun getRoute(routeId: UUID): Route {
        return repository.findOne(routeId)
    }

    override fun getRouteByTrackingCode(trackingCode: String): Route {
        return repository.findOneByTrackingCode(trackingCode)
    }

    override fun changeStatus(routeId: UUID, status: RouteStatus) {
        repository.updateStatus(routeId, status)
    }

    override fun listing(): List<Route> {
        return repository.findAll()
    }
}