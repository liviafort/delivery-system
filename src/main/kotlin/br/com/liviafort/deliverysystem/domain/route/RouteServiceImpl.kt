package br.com.liviafort.deliverysystem.domain.route

class RouteServiceImpl(private val repository: RouteRepository) : RouteService {
    override fun create(route: Route) {
        repository.save(route)
    }

    override fun changeStatus(route: Route, status: RouteStatus) {
        repository.updateStatus(route, status)
    }

    override fun listing(): List<Route> {
        return repository.findAll()
    }
}