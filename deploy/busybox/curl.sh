#!/bin/bash
curl -X POST "http://connect:8083/connectors" -H "Content-Type: application/json" -d @connector.json
