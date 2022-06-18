import React from "react";
import styled from "styled-components";
import LoginBtn from "../components/Btn/LoginBtn";
import SignUpBtn from "../components/Btn/SignUpBtn";

const FooterBanner = () => {
  return (

        <ThemeSection>
          <Themediv>
            <StyledH1>회원가입을 통해 오더캔버스를 만나보세요!</StyledH1>
            <StyledDesc>로그인 이후 서비스를 이용하실 수 있습니다.</StyledDesc>
            <LoginBtn/>
            <SignUpBtn/>
          </Themediv>
        </ThemeSection>
  );
};

const Themediv = styled.div`
    align-items: center;
    text-align:center;
    padding-top: 5rem;
    height: 550px;
    color: white;
`;

const ThemeSection = styled.div`
    display: flex-colum;
    width:100%;
    height:500px;
    padding: 10px;
    align-items: center;
    justify-items: center;
    object-fit: contain;
    background-image: url("/img/FooterBanner.svg");
    background-size: cover;
`; 

const StyledH1 = styled.h1`
    display: flex-colum;
    margin: 30px;
    padding: 3px;
    font-size: 1.5rem;
    font-weight: bold;
    text-align: center;
`;

const StyledDesc= styled.div`
    display: flex-colum;
    padding: 3px;
    margin: 15px;
`;

export default FooterBanner;
