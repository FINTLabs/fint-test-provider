# FINT test-provider

[![Build Status](https://travis-ci.org/FINTprosjektet/fint-test-provider.svg?branch=master)](https://travis-ci.org/FINTprosjektet/fint-test-provider)
[![Coverage Status](https://coveralls.io/repos/github/FINTprosjektet/fint-test-provider/badge.svg?branch=master)](https://coveralls.io/github/FINTprosjektet/fint-test-provider?branch=master)

## Adapter

To test the adapter (SSE) connection, use the `adapter` profile.

### Configuration
```
fint:
 adapter:
  organizations: mock.no
  sse-endpoint: http://localhost:8080/provider/sse/%s
  status-endpoint: http://localhost:8080/provider/status
  response-endpoint: http://localhost:8080/provider/response
```

Use swagger to send events (http://localhost:8080/provider/swagger-ui.html)

## Consumer

To test the consumer, start the consumer in test mode (fint-event test mode). Start the fint-test-provider with the `consumer` profile.