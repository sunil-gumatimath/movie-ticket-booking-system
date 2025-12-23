import urllib.request
import json

def hit_post(url, data):
    req = urllib.request.Request(url, data=json.dumps(data).encode('utf-8'), headers={'Content-Type': 'application/json'}, method='POST')
    try:
        with urllib.request.urlopen(req) as f:
            print(f"URL: {url}")
            print(f"Status: {f.status}")
            print(f"Body: {f.read().decode('utf-8')}")
    except Exception as e:
        print(f"Error hitting {url}: {e}")

# 1. Register User
user_data = {
    "username": "ted_user_99",
    "email": "teduser99@gmail.com",
    "phoneNumber": "9876543210",
    "password": "Password123!",
    "dateOfBirth": "1995-01-01",
    "userRole": "ROLE_USER"
}
hit_post("http://localhost:8080/register", user_data)

# 2. Register Theater Owner
owner_data = {
    "username": "ted_owner_99",
    "email": "tedowner99@gmail.com",
    "phoneNumber": "9988776655",
    "password": "Password123!",
    "dateOfBirth": "1990-01-01",
    "userRole": "ROLE_THEATER_OWNER"
}
hit_post("http://localhost:8080/register", owner_data)
