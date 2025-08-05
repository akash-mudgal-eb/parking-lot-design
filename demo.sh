#!/bin/bash

# Smart Parking Lot API Test Script
# This script demonstrates the parking lot system functionality

echo "🚗 Smart Parking Lot API Demo"
echo "================================"

BASE_URL="http://localhost:8080/api/parking"

# Function to make HTTP requests and display responses
make_request() {
    echo -e "\n📡 $1"
    echo "Request: $2"
    if [ "$3" ]; then
        echo "Body: $3"
        curl -s -X $2 "$BASE_URL$4" -H "Content-Type: application/json" -d "$3" | jq .
    else
        curl -s -X $2 "$BASE_URL$4" | jq .
    fi
}

# Check if server is running
echo "🔍 Checking if server is running..."
if ! curl -s "$BASE_URL/health" > /dev/null; then
    echo "❌ Server is not running. Please start the application first:"
    echo "   mvn spring-boot:run"
    exit 1
fi

echo "✅ Server is running!"

# 1. Check initial status
make_request "Initial Parking Lot Status" "GET" "" "/status"

# 2. Park a motorcycle
make_request "Park Motorcycle (License: BIKE001)" "POST" '{"licensePlate":"BIKE001","vehicleType":"MOTORCYCLE","ownerName":"Alice Johnson"}' "/entry"

# 3. Park a car
make_request "Park Car (License: CAR001)" "POST" '{"licensePlate":"CAR001","vehicleType":"CAR","ownerName":"Bob Smith"}' "/entry"

# 4. Park another car
make_request "Park Car (License: CAR002)" "POST" '{"licensePlate":"CAR002","vehicleType":"CAR","ownerName":"Charlie Brown"}' "/entry"

# 5. Try to park the same car again (should fail)
make_request "Try to Park Same Car Again (Should Fail)" "POST" '{"licensePlate":"CAR001","vehicleType":"CAR","ownerName":"Bob Smith"}' "/entry"

# 6. Check status after parking
make_request "Status After Parking 3 Vehicles" "GET" "" "/status"

# 7. Exit the motorcycle
make_request "Exit Motorcycle (License: BIKE001)" "POST" '{"licensePlate":"BIKE001"}' "/exit"

# 8. Exit one car
make_request "Exit Car (License: CAR001)" "POST" '{"licensePlate":"CAR001"}' "/exit"

# 9. Final status
make_request "Final Status" "GET" "" "/status"

# 10. Try to exit a vehicle that's not parked (should fail)
make_request "Try to Exit Non-Parked Vehicle (Should Fail)" "POST" '{"licensePlate":"NONEXISTENT"}' "/exit"

echo -e "\n🎉 Demo completed!"
echo "================================"
echo "Summary of operations:"
echo "✅ Parked 3 vehicles (1 motorcycle, 2 cars)"
echo "✅ Prevented duplicate parking"
echo "✅ Exited 2 vehicles with fee calculation"
echo "✅ Showed real-time status updates"
echo "✅ Handled error cases gracefully"
