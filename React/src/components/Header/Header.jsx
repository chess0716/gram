import React from "react";
import "./Header.css";
import Navbar from "../Navbar/Navbar";
import images from "../../constants/images";

const Header = () => (
    <div className="header" style={{
        background: `linear-gradient(rgba(0,0,0,0.7),
        rgba(0,0,0,0.2)), url(${images.header_bg}) center/cover no-repeat`
    }}>
        <Navbar/>

        <div className="container">
            <div className="header__content text__center
            text__light flex flex__center">
                <div className="header__content--left"></div>
                <div className="header__content--right">
                    <h1 className="header__title fw__6">
                        GRAM 당신의 즐거운 공간
                    </h1>
                    <br/>
                    <p className="para__text">
                    우리는 공간과 당신 을 연결 합니다
                    <br/>
                    우리의 서비스가 당신의삶 을 더욱 멋있게 했으면 좋겠습니다 :)
                    </p>
                    <br/>
                    <a href="#" className="btn btn__blue">
                        contact us
                    </a>
                </div>
            </div>
        </div>
    </div>
)

export default Header;  