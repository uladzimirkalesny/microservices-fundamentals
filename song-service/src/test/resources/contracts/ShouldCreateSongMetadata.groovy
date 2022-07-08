package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url("/v1/songs")
        body([
                name      : "We are the champions",
                artist    : "Queen",
                album     : "News of the world",
                length    : "2:59",
                resourceId: 1,
                year      : 1977
        ])
        headers {
            contentType applicationJson()
        }
    }
    response {
        status OK()
        body([
                id: 1
        ])
        headers {
            contentType applicationJson()
        }
    }
}
