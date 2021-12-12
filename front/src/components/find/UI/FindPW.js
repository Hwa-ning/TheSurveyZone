import React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { ThemeProvider } from '@mui/material/styles';
import ContactSupportIcon from '@mui/icons-material/ContactSupport';

const FindPW = ({theme, handleSubmit}) => {
    return (
        <>
            <ThemeProvider theme={theme}>
                <Container component="main" maxWidth="xs">
                    <CssBaseline />
                    <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                    >
                    <Avatar sx={{ m: 1 }}>
                        <ContactSupportIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        비밀번호 찾기
                    </Typography>
                        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                            <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="User_Email"
                            label="이메일"
                            type="email"
                            id="User_Email"
                            autoComplete="off"
                            />
                            <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="User_Name"
                            label="성함"
                            name="User_Name"
                            autoComplete="off"
                            autoFocus
                            />
                            <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="User_Tel"
                            label="전화번호"
                            type="tel"
                            id="User_Tel"
                            autoComplete="off"
                            />
                            <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            >
                            비밀번호 찾기
                            </Button>
                            <Grid container>
                            <Grid item xs>
                                <Link href="/FindIDPage" variant="body2">
                                    ID 찾기
                                </Link>
                            </Grid>
                            <Grid item>
                                <Link href="/RegisterPage" variant="body2">
                                    회원가입
                                </Link>
                            </Grid>
                            </Grid>
                        </Box>
                    </Box>
                </Container>
            </ThemeProvider><br/><br/><br/>
        </>
    );
};

export default FindPW;