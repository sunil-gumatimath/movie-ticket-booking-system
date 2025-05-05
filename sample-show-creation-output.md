# Sample Show Creation Process and Output

## Sample Input Data

```json
{
  "startTimeEpochMillis": 1718035200000,  // June 11, 2024, 10:00 AM UTC
  "movieId": "m123456789"
}
```

Screen ID: `screen123456789`

## Backend Process Flow

1. **Validate Screen**
   ```
   Found screen with ID: screen123456789
   Screen Type: STANDARD
   Capacity: 120
   Number of Rows: 10
   ```

2. **Get Associated Theater**
   ```
   Theater found: PVR Cinemas
   Theater ID: theater987654321
   ```

3. **Validate Movie**
   ```
   Movie found: "Inception"
   Movie ID: m123456789
   Runtime: 2 hours 28 minutes (148 minutes)
   ```

4. **Convert Start Time**
   ```
   Start Time (Epoch): 1718035200000
   Start Time (LocalDateTime): 2024-06-11T10:00:00 UTC
   ```

5. **Calculate End Time**
   ```
   End Time (LocalDateTime): 2024-06-11T12:28:00 UTC
   End Time (Epoch): 1718040480000
   ```

6. **Check for Overlapping Shows**
   ```
   Checking for overlaps with parameters:
   - Screen: screen123456789
   - End Time: 2024-06-11T12:28:00 UTC
   - Start Time: 2024-06-11T10:00:00 UTC
   
   No overlapping shows found. Time slot is available.
   ```

7. **Create and Save Show**
   ```
   Creating new show:
   - Screen: screen123456789
   - Theater: PVR Cinemas
   - Movie: Inception
   - Start Time: 2024-06-11T10:00:00 UTC
   - End Time: 2024-06-11T12:28:00 UTC
   
   Show saved successfully with ID: show123456789
   ```

## API Response

```json
{
  "status": "success",
  "message": "Show Created",
  "data": {
    "showId": "show123456789",
    "movieTitle": "Inception",
    "theaterName": "PVR Cinemas",
    "screenId": "screen123456789",
    "startTimeEpochMillis": 1718035200000,
    "endTimeEpochMillis": 1718040480000
  },
  "statusCode": 201
}
```

## Example of Overlapping Show Error

If we try to create another show that overlaps with the existing one:

```json
{
  "startTimeEpochMillis": 1718038800000,  // June 11, 2024, 11:00 AM UTC
  "movieId": "m987654321"
}
```

The system would detect the overlap and return:

```json
{
  "status": "error",
  "message": "The selected time slot is already occupied for this screen",
  "statusCode": 409
}
```

## Visual Timeline of Shows

```
Time (June 11, 2024)
|
|    10:00 AM                      12:28 PM
|    |---------------------------|    First Show: "Inception"
|
|         11:00 AM                       1:30 PM
|         |---------------------------| Second Show: "The Dark Knight" (OVERLAPS!)
|
|                                 12:30 PM                 3:00 PM
|                                 |----------------------| Third Show: "Interstellar" (OK)
|
```

In this example, the second show would be rejected due to overlap, but the third show would be accepted as it starts after the first show ends.
