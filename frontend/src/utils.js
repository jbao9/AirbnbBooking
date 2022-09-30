// 前后端通信的文件
const domain = "http://localhost:8080"; //测试阶段后端程序url 
//const domain = "https://excellent-grove-362222.wn.r.appspot.com";  //backend url after deployment

// proxy
//const domain = "";

//credentials = {username : string, password : string}
//asHost : boolean
export const login = (credential, asHost) => {
    const loginUrl = `${domain}/authenticate/${asHost ? "host" : "guest"}`;  //$代表{}里面的值被最上面的表达式替换
    //fetch(url, request)   
    return fetch(loginUrl, {
        method: "POST",
        headers: {  //request里的信息
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credential),
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to log in");
        }
     
        return response.json(); //convert to json if backend send back something
    });
};
    

export const register = (credential, asHost) => {
    const registerUrl = `${domain}/register/${asHost ? "host" : "guest"}`;
    return fetch(registerUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(credential),
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to register");
        }
    });
};

export const getReservations = () => {
    const authToken = localStorage.getItem("authToken");
    const listReservationsUrl = `${domain}/reservations`;
   
    return fetch(listReservationsUrl, { //默认GET method
        headers: {
        Authorization: `Bearer ${authToken}`,
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to get reservation list");
        }
   
        return response.json();
    });
};

export const getStaysByHost = () => {
    const authToken = localStorage.getItem("authToken");
    const listStaysUrl = `${domain}/stays/`;
   
    return fetch(listStaysUrl, {
        headers: {
        Authorization: `Bearer ${authToken}`,
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to get stay list");
        }
   
        return response.json();  
    });
};
  
export const searchStays = (query) => {  //给后端传参数，是放在url的 query string上
    const authToken = localStorage.getItem("authToken");
    //如果后端设置了跨域访问支持，用下面的
    //const searchStaysUrl = new URL(`${domain}/search/`);
    //如果后端没有设置Cross-Origin Resource Sharing support,用下面的
    const searchStaysUrl = new URL(`http://localhost:3000/search/`);
    searchStaysUrl.searchParams.append("guest_number", query.guest_number);
    searchStaysUrl.searchParams.append(
        "checkin_date",
        query.checkin_date.format("YYYY-MM-DD")
    );
    searchStaysUrl.searchParams.append(
        "checkout_date",
        query.checkout_date.format("YYYY-MM-DD")
    );
    searchStaysUrl.searchParams.append("lat", 37);
    searchStaysUrl.searchParams.append("lon", -122);
   
    return fetch(searchStaysUrl, {
        headers: {
        Authorization: `Bearer ${authToken}`,
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to search stays");
        }
   
        return response.json(); //处理回来的数据然后return
    });
};

export const deleteStay = (stayId) => {
    const authToken = localStorage.getItem("authToken");
    const deleteStayUrl = `${domain}/stays/${stayId}`;
   
    return fetch(deleteStayUrl, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${authToken}`,
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to delete stay");
        }
    });
  };
   
export const bookStay = (data) => {
    const authToken = localStorage.getItem("authToken");
    const bookStayUrl = `${domain}/reservations`;
   
    return fetch(bookStayUrl, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${authToken}`,
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to book reservation");
        }
    });
  };
   
export const cancelReservation = (reservationId) => {
    const authToken = localStorage.getItem("authToken");
    const cancelReservationUrl = `${domain}/reservations/${reservationId}`;
   
    return fetch(cancelReservationUrl, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${authToken}`,
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to cancel reservation");
        }
    });
  };
   
export const getReservationsByStay = (stayId) => {
    const authToken = localStorage.getItem("authToken");
    const getReservationByStayUrl = `${domain}/stays/reservations/${stayId}`;
   
    return fetch(getReservationByStayUrl, {
        headers: {
        Authorization: `Bearer ${authToken}`,
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to get reservations by stay");
        }
   
        return response.json();
    });
};
   
export const uploadStay = (data) => {
    const authToken = localStorage.getItem("authToken");
    const uploadStayUrl = `${domain}/stays`;
   
    return fetch(uploadStayUrl, {
        method: "POST",
        headers: {
         Authorization: `Bearer ${authToken}`,
        },
        body: data,
    }).then((response) => {
        if (response.status !== 200) {
            throw Error("Fail to upload stay");
        }
    });
};


  
  