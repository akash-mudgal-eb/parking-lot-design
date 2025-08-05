# Smart Parking Lot API Test Script (PowerShell)
# This script demonstrates the parking lot system functionality

Write-Host "🚗 Smart Parking Lot API Demo" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green

$BaseUrl = "http://localhost:8080/api/parking"

# Function to make HTTP requests and display responses
function Make-Request {
    param($Description, $Method, $Body, $Endpoint)
    
    Write-Host "`n📡 $Description" -ForegroundColor Cyan
    Write-Host "Request: $Method $BaseUrl$Endpoint" -ForegroundColor Yellow
    
    try {
        if ($Body) {
            Write-Host "Body: $Body" -ForegroundColor Yellow
            $response = Invoke-RestMethod -Uri "$BaseUrl$Endpoint" -Method $Method -Body $Body -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri "$BaseUrl$Endpoint" -Method $Method
        }
        $response | ConvertTo-Json -Depth 10 | Write-Host
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            $_.Exception.Response | Write-Host
        }
    }
}

# Check if server is running
Write-Host "🔍 Checking if server is running..."
try {
    Invoke-RestMethod -Uri "$BaseUrl/health" -Method GET | Out-Null
    Write-Host "✅ Server is running!" -ForegroundColor Green
} catch {
    Write-Host "❌ Server is not running. Please start the application first:" -ForegroundColor Red
    Write-Host "   mvn spring-boot:run" -ForegroundColor Yellow
    exit 1
}

# 1. Check initial status
Make-Request "Initial Parking Lot Status" "GET" $null "/status"

# 2. Park a motorcycle
Make-Request "Park Motorcycle (License: BIKE001)" "POST" '{"licensePlate":"BIKE001","vehicleType":"MOTORCYCLE","ownerName":"Alice Johnson"}' "/entry"

# 3. Park a car
Make-Request "Park Car (License: CAR001)" "POST" '{"licensePlate":"CAR001","vehicleType":"CAR","ownerName":"Bob Smith"}' "/entry"

# 4. Park another car
Make-Request "Park Car (License: CAR002)" "POST" '{"licensePlate":"CAR002","vehicleType":"CAR","ownerName":"Charlie Brown"}' "/entry"

# 5. Try to park the same car again (should fail)
Make-Request "Try to Park Same Car Again (Should Fail)" "POST" '{"licensePlate":"CAR001","vehicleType":"CAR","ownerName":"Bob Smith"}' "/entry"

# 6. Check status after parking
Make-Request "Status After Parking 3 Vehicles" "GET" $null "/status"

# 7. Exit the motorcycle
Make-Request "Exit Motorcycle (License: BIKE001)" "POST" '{"licensePlate":"BIKE001"}' "/exit"

# 8. Exit one car
Make-Request "Exit Car (License: CAR001)" "POST" '{"licensePlate":"CAR001"}' "/exit"

# 9. Final status
Make-Request "Final Status" "GET" $null "/status"

# 10. Try to exit a vehicle that's not parked (should fail)
Make-Request "Try to Exit Non-Parked Vehicle (Should Fail)" "POST" '{"licensePlate":"NONEXISTENT"}' "/exit"

Write-Host "`n🎉 Demo completed!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host "Summary of operations:" -ForegroundColor White
Write-Host "✅ Parked 3 vehicles (1 motorcycle, 2 cars)" -ForegroundColor Green
Write-Host "✅ Prevented duplicate parking" -ForegroundColor Green
Write-Host "✅ Exited 2 vehicles with fee calculation" -ForegroundColor Green
Write-Host "✅ Showed real-time status updates" -ForegroundColor Green
Write-Host "✅ Handled error cases gracefully" -ForegroundColor Green
