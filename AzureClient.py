# -*- coding: utf-8 -*-
"""
Created on Sat Dec  7 11:11:52 2024

@author: brand
"""

import tkinter as tk
from tkinter import ttk, messagebox
import requests
import datetime

# Base URLs for APIs
BASE_URL = "http://20.0.123.254:8080/RESTService/webresources"
ROOM_API_BASE_URL = f"{BASE_URL}/room"
WEATHER_API_BASE_URL = f"{BASE_URL}/weather"
POSTCODE_API_BASE_URL = f"{BASE_URL}/postcode"
ACCOUNT_API_BASE_URL = f"{BASE_URL}/account/all"
ACCOUNT_EDIT_API_URL = f"{BASE_URL}/account/edit"
DISTANCE_API_BASE_URL = f"{BASE_URL}/distance"

# Global variable to track the logged-in user
logged_in_user = None


def verify_login(username, password):
    try:
        response = requests.get(ACCOUNT_API_BASE_URL)
        if response.status_code == 200:
            accounts = response.json()
            for account in accounts:
                if account["username"] == username and account["password"] == password:
                    return account  # Return the account if login is successful
            return None  # Invalid credentials
        else:
            messagebox.showerror("Error", "Failed to fetch accounts.")
            return None
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        return None


def show_login():
    def attempt_login():
        username = username_entry.get().strip()
        password = password_entry.get().strip()
        if not username or not password:
            messagebox.showerror("Error", "Please enter both username and password.")
            return

        global logged_in_user
        logged_in_user = verify_login(username, password)
        if logged_in_user:
            login_frame.pack_forget()
            initialize_main_application()
            show_main_application()
        else:
            messagebox.showerror("Error", "Invalid username or password.")

    login_frame.pack(fill=tk.BOTH, expand=True)

    ttk.Label(login_frame, text="Login", font=("Arial", 18)).pack(pady=10)
    ttk.Label(login_frame, text="Username:").pack(pady=5)
    username_entry = ttk.Entry(login_frame, width=30)
    username_entry.pack(pady=5)
    ttk.Label(login_frame, text="Password:").pack(pady=5)
    password_entry = ttk.Entry(login_frame, width=30, show="*")
    password_entry.pack(pady=5)
    login_button = ttk.Button(login_frame, text="Login", command=attempt_login)
    login_button.pack(pady=10)


def initialize_main_application():
    global main_frame, city_entry, search_button, results_frame

    for widget in main_frame.winfo_children():
        widget.destroy()

    user_action_frame = ttk.Frame(main_frame)
    user_action_frame.pack(fill=tk.X, padx=10, pady=5, anchor=tk.NE)

    view_applications_button = ttk.Button(user_action_frame, text="View Applications", command=view_applications)
    view_applications_button.pack(side=tk.RIGHT, padx=5)

    logout_button = ttk.Button(user_action_frame, text="Logout", command=logout)
    logout_button.pack(side=tk.RIGHT, padx=5)

    input_frame = ttk.Frame(main_frame, padding="10")
    input_frame.pack(fill=tk.X)

    ttk.Label(input_frame, text="City:").pack(side=tk.LEFT, padx=5)
    city_entry = ttk.Entry(input_frame, width=30)
    city_entry.pack(side=tk.LEFT, padx=5)
    search_button = ttk.Button(input_frame, text="Search", command=search_by_city)
    search_button.pack(side=tk.LEFT, padx=5)

    results_canvas = tk.Canvas(main_frame)
    results_canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

    scrollbar = ttk.Scrollbar(main_frame, orient="vertical", command=results_canvas.yview)
    scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

    results_canvas.configure(yscrollcommand=scrollbar.set)
    results_frame = ttk.Frame(results_canvas)
    results_canvas.create_window((0, 0), window=results_frame, anchor="nw")
    results_frame.bind("<Configure>", lambda e: results_canvas.configure(scrollregion=results_canvas.bbox("all")))


def show_main_application():
    main_frame.pack(fill=tk.BOTH, expand=True)


def logout():
    global logged_in_user
    logged_in_user = None

    for widget in main_frame.winfo_children():
        widget.destroy()
    main_frame.pack_forget()

    login_frame.pack(fill=tk.BOTH, expand=True)


def view_applications():
    if not logged_in_user:
        messagebox.showerror("Error", "No user is logged in.")
        return

    applications = logged_in_user.get("applications", [])
    if not applications:
        messagebox.showinfo("Info", "No applications found.")
        return

    applications_window = tk.Toplevel(root)
    applications_window.title("Your Applications")
    applications_window.geometry("600x500")

    ttk.Label(applications_window, text="Your Applications", font=("Arial", 16)).pack(pady=10)
    applications_frame = ttk.Frame(applications_window, padding="10")
    applications_frame.pack(fill=tk.BOTH, expand=True)

    columns = ("Application ID", "Room ID", "Status", "Application Date", "Decision Date")
    applications_tree = ttk.Treeview(applications_frame, columns=columns, show="headings")
    for col in columns:
        applications_tree.heading(col, text=col)
        applications_tree.column(col, anchor=tk.W, width=120)
    applications_tree.pack(fill=tk.BOTH, expand=True)

    for app in applications:
        applications_tree.insert(
            "",
            tk.END,
            values=(
                app.get("applicationId", "N/A"),
                app.get("roomId", "N/A"),
                app.get("status", "N/A"),
                app.get("applicationDate", "N/A"),
                app.get("decisionDate", "N/A"),
            ),
        )

    button_frame = ttk.Frame(applications_window, padding="10")
    button_frame.pack(fill=tk.X)

    accept_button = ttk.Button(button_frame, text="Accept", command=lambda: update_application_status(applications_tree, "accepted"))
    accept_button.pack(side=tk.LEFT, padx=5)

    reject_button = ttk.Button(button_frame, text="Reject", command=lambda: update_application_status(applications_tree, "rejected"))
    reject_button.pack(side=tk.LEFT, padx=5)

    cancel_button = ttk.Button(button_frame, text="Cancel Application", command=lambda: update_application_status(applications_tree, "cancelled"))
    cancel_button.pack(side=tk.LEFT, padx=5)

    leave_button = ttk.Button(button_frame, text="Leave", command=applications_window.destroy)
    leave_button.pack(side=tk.RIGHT, padx=5)


def update_application_status(applications_tree, new_status):
    selected_item = applications_tree.selection()
    if not selected_item:
        messagebox.showerror("Error", "Please select an application.")
        return

    app_id = int(applications_tree.item(selected_item, "values")[0])

    for app in logged_in_user.get("applications", []):
        if app["applicationId"] == app_id:
            app["status"] = new_status
            if new_status in ["accepted", "rejected"]:
                app["decisionDate"] = datetime.date.today().isoformat()
            break
    else:
        messagebox.showerror("Error", "Application not found.")
        return

    try:
        response = requests.put(ACCOUNT_EDIT_API_URL, json=logged_in_user)
        if response.status_code == 200:
            applications_tree.item(selected_item, values=(
                app["applicationId"],
                app["roomId"],
                app["status"],
                app["applicationDate"],
                app["decisionDate"],
            ))
            messagebox.showinfo("Success", f"Application status updated to '{new_status}'.")
        else:
            messagebox.showerror("Error", "Failed to update the application.")
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")


def search_by_city():
    city = city_entry.get().strip()
    if not city:
        messagebox.showerror("Error", "Please enter a city name.")
        return

    url = f"{ROOM_API_BASE_URL}?city={city}"
    try:
        response = requests.get(url)
        if response.status_code == 200:
            rooms = response.json()
            for room in rooms:
                add_postcode_data_and_weather(room)
            display_rooms(rooms)
        else:
            error_message = response.json().get("message", "Failed to fetch rooms.")
            messagebox.showerror("Error", error_message)
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")


def add_postcode_data_and_weather(room):
    location = room.get("location", {})
    postcode = location.get("postcode")

    if not postcode:
        room["postcode_data"] = "Postcode data unavailable"
        room["weather"] = "Weather data unavailable"
        return

    postcode_url = f"{POSTCODE_API_BASE_URL}?postcode={postcode.replace(' ', '+')}"
    try:
        postcode_response = requests.get(postcode_url)
        if postcode_response.status_code == 200:
            postcode_data = postcode_response.json()
            lat = postcode_data.get("latitude")
            lon = postcode_data.get("longitude")

            if lat and lon:
                room["postcode_data"] = (
                    f"Latitude: {lat}, Longitude: {lon}, "
                    f"Region: {postcode_data.get('region', 'N/A')}"
                )
                add_weather_data(room, lat, lon)
            else:
                room["postcode_data"] = "Postcode data incomplete"
                room["weather"] = "Weather data unavailable"
        else:
            room["postcode_data"] = "Postcode data unavailable"
            room["weather"] = "Weather data unavailable"
    except Exception as e:
        room["postcode_data"] = f"Error fetching postcode data: {e}"
        room["weather"] = "Weather data unavailable"


def add_weather_data(room, lat, lon):
    weather_url = f"{WEATHER_API_BASE_URL}?lat={lat}&lon={lon}"
    try:
        weather_response = requests.get(weather_url)
        if weather_response.status_code == 200:
            weather_data = weather_response.json()
            room["weather"] = (
                f"Weather: {weather_data.get('weather', 'N/A')}, "
                f"Max Temp: {weather_data.get('temp2m', {}).get('max', 'N/A')}°C, "
                f"Wind: {weather_data.get('wind10m_max', 'N/A')} m/s"
            )
        else:
            room["weather"] = "Weather data unavailable"
    except Exception as e:
        room["weather"] = f"Error fetching weather: {e}"

def add_crime_stats(location):
    postcode_data = location.get("postcode_data", "")
    lat, lon = parse_lat_lon_from_postcode(postcode_data)

    if not lat or not lon:
        messagebox.showerror("Error", "Latitude and longitude are required for crime stats.")
        return

    three_months_ago = datetime.date.today().replace(day=1) - datetime.timedelta(days=90)
    date = three_months_ago.strftime("%Y-%m")

    url = f"http://localhost:8080/RESTService/webresources/crime?lat={lat}&lon={lon}&date={date}"
    try:
        response = requests.get(url)
        if response.status_code == 200:
            crime_data = response.json()
            display_crime_stats(crime_data)
        else:
            error_message = response.json().get("message", "Failed to fetch crime stats.")
            messagebox.showerror("Error", error_message)
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")



def display_crime_stats(crime_data):
    crime_window = tk.Toplevel(root)
    crime_window.title("Crime Stats")
    crime_window.geometry("400x300")

    ttk.Label(crime_window, text="Crime Statistics", font=("Arial", 16)).pack(pady=10)

    for category, count in crime_data.items():
        ttk.Label(crime_window, text=f"{category}: {count}").pack(anchor=tk.W)

    ttk.Button(crime_window, text="Close", command=crime_window.destroy).pack(pady=10)


def display_rooms(rooms):
    for widget in results_frame.winfo_children():
        widget.destroy()

    if not rooms:
        messagebox.showinfo("No Results", "No rooms found for the given city.")
        return

    for idx, room in enumerate(rooms, start=1):
        room_frame = ttk.LabelFrame(results_frame, text=f"Room {idx}", padding="10")
        room_frame.pack(fill=tk.X, padx=10, pady=5)

        location = room.get("location", {})
        details = room.get("details", {})
        amenities = ", ".join(details.get("amenities", []))
        spoken_languages = ", ".join(room.get("spoken_languages", []))

        ttk.Label(room_frame, text=f"ID: {room.get('id', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Name: {room.get('name', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"City: {location.get('city', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Postcode: {location.get('postcode', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Furnished: {details.get('furnished', False)}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Shared With: {details.get('shared_with', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Price/Month: £{room.get('price_per_month_gbp', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Amenities: {amenities}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Spoken Languages: {spoken_languages}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Weather: {room.get('weather', 'N/A')}").pack(anchor=tk.W)
        ttk.Label(room_frame, text=f"Postcode Data: {room.get('postcode_data', 'N/A')}").pack(anchor=tk.W)

        apply_button = ttk.Button(room_frame, text="Apply", command=lambda rid=room.get("id"): apply_for_room(rid))
        apply_button.pack(pady=5)

        distance_button = ttk.Button(
            room_frame,
            text="Calculate Distance",
            command=lambda postcode_data=room.get("postcode_data", "N/A"): open_distance_calculator(postcode_data)
        )
        distance_button.pack(pady=5)

        crime_button = ttk.Button(
            room_frame,
            text="View Crime Stats",
            command=lambda location=room: add_crime_stats(location)
        )
        crime_button.pack(pady=5)


def open_distance_calculator(postcode_data):
    """
    Opens a new window for the user to enter the starting latitude and longitude.
    Fetches the destination latitude and longitude from the postcode data and calculates the distance.
    """
    if "Latitude" not in postcode_data or "Longitude" not in postcode_data:
        messagebox.showerror("Error", "Invalid postcode data.")
        return

    # Parse destination latitude and longitude from postcode_data
    dest_lat, dest_lon = parse_lat_lon_from_postcode(postcode_data)

    def calculate_distance():
        try:
            start_lat = float(start_lat_entry.get().strip())
            start_lon = float(start_lon_entry.get().strip())
        except ValueError:
            result_label.config(text="Invalid input. Enter valid latitude and longitude.")
            return

        url = f"{DISTANCE_API_BASE_URL}?startLat={start_lat}&startLon={start_lon}&endLat={dest_lat}&endLon={dest_lon}"
        try:
            response = requests.get(url)
            if response.status_code == 200:
                distance_data = response.json()
                result_label.config(
                    text=f"Distance: {distance_data['distance_km']} km\nDuration: {distance_data['duration']}"
                )
            else:
                result_label.config(text="Failed to fetch distance data.")
        except Exception as e:
            result_label.config(text=f"Error: {e}")

    distance_window = tk.Toplevel(root)
    distance_window.title("Calculate Distance")
    distance_window.geometry("400x300")

    ttk.Label(distance_window, text="Enter Starting Coordinates").pack(pady=10)

    ttk.Label(distance_window, text="Latitude:").pack()
    start_lat_entry = ttk.Entry(distance_window, width=30)
    start_lat_entry.pack()

    ttk.Label(distance_window, text="Longitude:").pack()
    start_lon_entry = ttk.Entry(distance_window, width=30)
    start_lon_entry.pack()

    calculate_button = ttk.Button(distance_window, text="Calculate", command=calculate_distance)
    calculate_button.pack(pady=10)

    result_label = ttk.Label(distance_window, text="")
    result_label.pack(pady=10)


def parse_lat_lon_from_postcode(postcode_data):
    """
    Extract latitude and longitude from the postcode data string.
    Assumes postcode data is formatted as "Latitude: XX.XXX, Longitude: YY.YYY, Region: ZZZ".
    """
    try:
        parts = postcode_data.split(", ")
        lat = float(parts[0].split(": ")[1])
        lon = float(parts[1].split(": ")[1])
        return lat, lon
    except (IndexError, ValueError):
        return None, None



def apply_for_room(room_id):
    def submit_application():
        application_data = {
            "applicationId": len(logged_in_user.get("applications", [])) + 1,
            "roomId": room_id,
            "status": "pending",
            "applicationDate": datetime.date.today().isoformat(),
            "decisionDate": None,
        }
        logged_in_user.setdefault("applications", []).append(application_data)

        try:
            response = requests.put(ACCOUNT_EDIT_API_URL, json=logged_in_user)
            if response.status_code == 200:
                messagebox.showinfo("Success", "Application submitted successfully!")
                application_window.destroy()
            else:
                messagebox.showerror("Error", "Failed to submit application.")
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {e}")

    application_window = tk.Toplevel(root)
    application_window.title("Apply for Room")
    application_window.geometry("300x200")

    ttk.Label(application_window, text=f"Applying for Room ID: {room_id}").pack(pady=10)
    ttk.Button(application_window, text="Submit Application", command=submit_application).pack(pady=20)


# Root Tkinter Window
root = tk.Tk()
root.title("Service Cloud Computing Client")
root.geometry("800x600")  # Enlarged window size

login_frame = ttk.Frame(root, padding="10")
main_frame = ttk.Frame(root, padding="10")

show_login()
root.mainloop()
