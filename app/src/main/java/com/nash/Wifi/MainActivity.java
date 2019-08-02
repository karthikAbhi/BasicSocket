package com.nash.Wifi;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.nash.mywifiprinterlibrary.BarcodeType;
import com.nash.mywifiprinterlibrary.CutCommand;
import com.nash.mywifiprinterlibrary.FunctionType;
import com.nash.mywifiprinterlibrary.MyWifiPrinter;
import com.nash.mywifiprinterlibrary.NPOSWifiConnection;
import com.nash.mywifiprinterlibrary.PDF417ErrorCorrectionLevel;
import com.nash.mywifiprinterlibrary.PDF417ErrorCorrectionMode;
import com.nash.mywifiprinterlibrary.PDF417Options;
import com.nash.mywifiprinterlibrary.QRErrCorrLvl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button mBtnStart, mBtnConnect, mBtnCommand, mBtnClose;
    byte[] mBuffer = new byte[10];
    Socket mSocket;
    BufferedInputStream mBufferedInputStream;
    BufferedOutputStream mBufferedOutputStream;
    boolean mSocketState;


    // Newly Added

    private MyWifiPrinter mMyWifiPrinter;
    private Context mContext;

    // --- UI - Button References ---

    private Button mCloseBTN, mDiscoverBTN;
    private Button mPrintButton;
    private Button mLFCommandButton;
    private Button mFFCommandButton;
    private Button mPMCommandButton;
    private Button mSMCommandButton;
    private Button mHTCommandButton;
    private Button mInitializePrinterCommandButton;
    private Button mGSMFCommandButton;
    private Button mCutFormCommandButton;
    private Button mFFIPIXELSCommandButton;
    private Button mPDFF2FCommandButton;
    private Button mPDPMCommandButton;
    private Button mPDFNLCommandButton;
    private Button mSLFCommandButton;
    private Button mSGRSCCommandButton;
    private Button mSPPMCommandButton;
    private Button mSelectFontCommandButton;
    private Button mSetLeftMarginCommandButton;
    private Button mSetWidthOfPrintAreaCommandButton;
    private Button mSpecifyPrintAreaOnPageModeCommandButton;
    private Button mSetPhysicalPositionCommandButton;
    private Button mSetLogicalPositionCommandButton;
    private Button mSetVerticalPhysicalPositionOnPageModeCommandButton;
    private Button mSelectLSB_MSBDirectionInImageCommandButton;
    private Button mSetVerticalLogicalPositionOnPageModeCommandButton;
    private Button mSetPrintPositionOfHRICharCommandButton;
    private Button mSelectHRICharacterSizeCommandButton;
    private Button mSetBarcodeHeightCommandButton;
    private Button mSetBarcodeWidthCommandButton;
    private Button mSetNWAspectBarcode;
    private Button mPrintBarcodeButton;
    private Button mSetPrintDensity;
    private Button mInformSysTimeOfHostCommandButton;
    private Button mPrintBitImageCommandButton;
    private Button mPrintRasterBitImageCommandButton;
    private Button mPrintNVBitImageCommandButton;
    private Button mSelectNVBitImageCommandButton;
    private Button mDefineNVBitImageCommandButton;
    private Button mtUOnOffCommandButton;
    private Button mSDLSCommandButton;
    private Button mTEMOnOffCommandButton;
    private Button mSPDPMCommandButton;
    private Button mSJCommandButton;
    private Button mQRCodeCommandButton;
    private Button mTurnWBRPOnOffCommandButton;
    private Button mSCMCPCommandButton;
    private Button mPDF417CommandButton;

    private RadioGroup mRadioGroupQR;
    private RadioButton mRadioButtonQR;

    private RadioGroup mRadioGroupFT;
    private RadioButton mRadioButtonFT;
    private RadioGroup mRadioGroupCT;
    private RadioButton mRadioButtonCT;

    private Spinner mBarcodeSpinner;
    private int mBarcodeTypeSelected;

    //Control Transfer
    private Button mControlTransfer;

    /**
     * UI - EditText References
     */
    private EditText mSampleTextEditText;
    private EditText mPDFF2FEditText;
    private EditText mPDFNLEditText;
    private EditText mSLFEditText;
    private EditText mSGRSCEditText;
    private EditText mSPPMEditText;
    private EditText mSelectFontEditText;
    private EditText mSetLeftMarginEditText;
    private EditText mSetWidthOfPrintAreaEditText;
    private EditText mSPAPMxaxisEditText, mSPAPMyaxisEditText, mSPAPMxlengthEditText,
            mSPAPMylengthEditText;
    private EditText mSetPhysicalPositionEditText;
    private EditText mSetLogicalPositionEditText;
    private EditText mSetVerticalPhysicalPositionOnPageModeEditText;
    private EditText mSetVerticalLogicalPositionOnPageModeEditText;
    //Raster Bit - Printing
    private EditText mModeOfRasterBitEditText;
    //NV Bit - Printing
    private EditText mSelectPNVBitEditText, mModePNVBitEditText;
    //NV Bit - Selection
    private EditText mSelectSNVBitEditText, mModeSNVBitEditText, mOffsetSNVBitEditText;
    private EditText mNumberOfNVBitImagesEditText;
    private EditText mSelectLSB_MSBDirectionInImageEditText;
    private EditText mSetPrintPositionOfHRICharEditText;
    private EditText mSelectHRICharacterSizeEditText;
    private EditText mSetBarcodeHeightEditText;
    private EditText mSetBarcodeWidthEditText;
    private EditText mSetNWAspectBarcodeEditText;
    private EditText mBarcodeDataEditText;
    private EditText mSetPrintDensityEditText;
    private EditText mFFIPIXELSEditText;
    private EditText mUnderLineEditText;
    private EditText mTEMOnOffEditText;
    private EditText mSPDPMEditText;
    private EditText mSJEditText;
    private EditText mQRSizeEditText, mQRUserDataEditText;
    private EditText mTurnWBRPOnOffEditText;
    private EditText mSCMCPEditText;

    //TODO Control Transfer
    private EditText mControlTransferEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnStart = findViewById(R.id.btn_start);
        mBtnConnect = findViewById(R.id.btn_connect);
        mBtnCommand = findViewById(R.id.btn_command);
        mBtnClose = findViewById(R.id.btn_close);

        //Newly Added

        mContext = getApplicationContext();

        mMyWifiPrinter = new MyWifiPrinter(mContext) {
            @Override
            public void transfer(byte[] dataToPrintInBytes) {
                new sendCommandATask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dataToPrintInBytes);
            }
        };

        // --- UI Components - EditText Reference Creation ---

        mSampleTextEditText = findViewById(R.id.sampleTextEditText);
        mPDFF2FEditText = findViewById(R.id.pdff2fEditText);
        mPDFNLEditText = findViewById(R.id.pdfnlEditText);
        mSLFEditText = findViewById(R.id.slfEditText);
        mSGRSCEditText = findViewById(R.id.sgrscEditText);
        mSPPMEditText = findViewById(R.id.sppmEditText);
        mSelectFontEditText = findViewById(R.id.selFontEditText);
        mSetLeftMarginEditText = findViewById(R.id.setLeftMarginEditText);
        mSetWidthOfPrintAreaEditText = findViewById(R.id.setWidthOfPrintAreaEditText);
        mSPAPMxaxisEditText = findViewById(R.id.spapmxaxisEditText);
        mSPAPMyaxisEditText = findViewById(R.id.spapmyaxisEditText);
        mSPAPMxlengthEditText = findViewById(R.id.spapmxlengthEditText);
        mSPAPMylengthEditText = findViewById(R.id.spapmylengthEditText);
        mSetPhysicalPositionEditText = findViewById(R.id.setPhysicalPositionEditText);
        mSetLogicalPositionEditText = findViewById(R.id.setLogicalPositionEditText);
        mSetVerticalPhysicalPositionOnPageModeEditText = findViewById(R.id.setVPPOnPageModeEditText);
        mSetVerticalLogicalPositionOnPageModeEditText = findViewById(R.id.setVLPOnPageModeEditText);
        mModeOfRasterBitEditText = findViewById(R.id.modeOfRasterBitEditText);
        mSelectPNVBitEditText = findViewById(R.id.selectPNVBitEditText);
        mModePNVBitEditText = findViewById(R.id.modePNVBitEditText);
        mSelectSNVBitEditText = findViewById(R.id.selectSNVBitEditText);
        mModeSNVBitEditText = findViewById(R.id.modeSNVBitEditText);
        mOffsetSNVBitEditText = findViewById(R.id.offsetSNVBitEditText);
        mNumberOfNVBitImagesEditText = findViewById(R.id.numberOfNVBitImagesEditText);
        mSelectLSB_MSBDirectionInImageEditText = findViewById(R.id.selectLSB_MSBDirInImgEditText);
        mSetPrintPositionOfHRICharEditText = findViewById(R.id.setPosHRICharEditText);
        mSelectHRICharacterSizeEditText = findViewById(R.id.selectHRICharSizeEditText);
        mSetBarcodeHeightEditText = findViewById(R.id.setBarcodeHeightEditText);
        mSetBarcodeWidthEditText = findViewById(R.id.setBarcodeWidthEditText);
        mSetNWAspectBarcodeEditText = findViewById(R.id.setNWAspectOfBarcodeEditText);
        mBarcodeDataEditText = findViewById(R.id.barcodeDataEditText);
        mSetPrintDensityEditText = findViewById(R.id.setPrintDensityEditText);
        mFFIPIXELSEditText = findViewById(R.id.ffinPixelsEditText);
        mUnderLineEditText = findViewById(R.id.underLineEditText);
        mTEMOnOffEditText = findViewById(R.id.turnEmphasizedOnOffEditText);
        mSPDPMEditText = findViewById(R.id.selectPrintDirInPageModeEditText);
        mSJEditText = findViewById(R.id.selectJustificationEditText);
        mQRSizeEditText = findViewById(R.id.qrSizeEditText);
        mQRUserDataEditText = findViewById(R.id.qrUserDataEditText);
        mTurnWBRPOnOffEditText = findViewById(R.id.turnBlackWhiteRevPrintModeOnOffEditText);
        mSCMCPEditText = findViewById(R.id.selectCutModeCutPaperEditText);

        // --- UI Components - Button Reference Creation ---
        //Print Text
        mPrintButton = findViewById(R.id.printButton);
        //Print Barcode
        //mPrintBarcodeButton = findViewById(R.id.printBarcodeButton);
        //LF Command
        mLFCommandButton = findViewById(R.id.lfCommandButton);
        //FF Command
        mFFCommandButton = findViewById(R.id.ffCommandButton);
        //Print data and Feed form to forward Command
        mPDFF2FCommandButton = findViewById(R.id.pdff2fButton);
        //Print data on feed n lines Command
        mPDFNLCommandButton = findViewById(R.id.pdfnlButton);
        //Print data on page mode Command
        mPDPMCommandButton = findViewById(R.id.pdpmButton);
        //Set the line feed amount Command
        mSLFCommandButton = findViewById(R.id.slfButton);
        //Set the gap of right side of the characters Command (14.07)
        mSGRSCCommandButton = findViewById(R.id.sgrscButton);
        //Set parameters of Print mode Command (14.08)
        mSPPMCommandButton = findViewById(R.id.sppmButton);
        //Select the font Command (14.09)
        mSelectFontCommandButton = findViewById(R.id.selFontButton);
        //Page Mode Command (14.13)
        mPMCommandButton = findViewById(R.id.pageModeButton);
        //Standard Mode Command (14.14)
        mSMCommandButton = findViewById(R.id.standardModeButton);
        //Horizontal Tab Command (14.15)
        mHTCommandButton = findViewById(R.id.horizontalTabButton);
        //Set the left side margin Command (14.16)
        mSetLeftMarginCommandButton = findViewById(R.id.setLeftMarginButton);
        //Set the width of print area Command (14.17)
        mSetWidthOfPrintAreaCommandButton = findViewById(R.id.setWidthOfPrintAreaButton);
        //Set the print area on page mode (14.18)
        mSpecifyPrintAreaOnPageModeCommandButton = findViewById(R.id.specifyPrintAreaOnPageModeButton);
        //Set the physical position (14.19)
        mSetPhysicalPositionCommandButton = findViewById(R.id.setPhysicalPositionButton);
        //Set the logical position (14.20)
        mSetLogicalPositionCommandButton = findViewById(R.id.setLogicalPositionButton);
        //Set the vertical physical position in page mode (14.21)
        mSetVerticalPhysicalPositionOnPageModeCommandButton = findViewById(R.id.setVPPOnPageModeButton);
        //Set the vertical logical position in page mode (14.22)
        mSetVerticalLogicalPositionOnPageModeCommandButton = findViewById(R.id.setVLPOnPageModeButton);
        //Print bit image (14.23)
        mPrintBitImageCommandButton = findViewById(R.id.printBitImageButton);
        //Print raster bit image (14.24)
        mPrintRasterBitImageCommandButton = findViewById(R.id.printRasterBitImageButton);
        //Print NV Bit Image (14.25)
        mPrintNVBitImageCommandButton = findViewById(R.id.printNVBitImageButton);
        //Select NV Bit Image (14.25)
        mSelectNVBitImageCommandButton = findViewById(R.id.selectNVBitImageButton);
        //Define NV Bit Image (14.26)
        mDefineNVBitImageCommandButton = findViewById(R.id.defineNVBitImageButton);
        //Select LSB-MSB direction in image (14.27)
        mSelectLSB_MSBDirectionInImageCommandButton = findViewById(R.id.selectLSB_MSBDirInImgButton);
        //Set print position of HRI characters (14.28)
        mSetPrintPositionOfHRICharCommandButton = findViewById(R.id.setPosHRICharButton);
        //Select HRI character size (14.29)
        mSelectHRICharacterSizeCommandButton = findViewById(R.id.selectHRICharSizeButton);
        //Set barcode height (14.30)
        mSetBarcodeHeightCommandButton = findViewById(R.id.setBarcodeHeightButton);
        //Set barcode width (14.31)
        mSetBarcodeWidthCommandButton = findViewById(R.id.setBarcodeWidthButton);
        //Set N:W aspect of the barcode (14.32)
        mSetNWAspectBarcode = findViewById(R.id.setNWAspectOfBarcodeButton);
        //Barcode Spinner
        mBarcodeSpinner = findViewById(R.id.spinner_barcode_types);
        //Print Barcode (14.33)
        mPrintBarcodeButton = findViewById(R.id.printBarcodeButton);
        //Initialize Printer Command
        mInitializePrinterCommandButton = findViewById(R.id.initializePrinterButton);
        //Set print density (14.40)
        mSetPrintDensity = findViewById(R.id.setPrintDensityButton);
        //Cue the Marked form Command
        mGSMFCommandButton = findViewById(R.id.gsMFButton);
        //Cut form Command
        mCutFormCommandButton = findViewById(R.id.cutFormButton);
        //Inform system time of the host (14.51)
        mInformSysTimeOfHostCommandButton = findViewById(R.id.infoSysTimeHostButton);
        //Feed form in pixels Command (14.54)
        mFFIPIXELSCommandButton = findViewById(R.id.ffinPixelsButton);
        //Turn Underline button ON/OFF (14.70)
        mtUOnOffCommandButton = findViewById(R.id.underLineButton);
        //Select default line spacing (14.71)
        mSDLSCommandButton = findViewById(R.id.setDefaultLineSpaceButton);
        //Turn emphasized mode on/off (14.72)
        mTEMOnOffCommandButton = findViewById(R.id.turnEmphasizedOnOffButton);
        //Select print direction in page mode (14.73)
        mSPDPMCommandButton = findViewById(R.id.selectPrintDirInPageModeButton);
        //Select justification (14.74)
        mSJCommandButton = findViewById(R.id.selectJustificationButton);
        //Print QR Code (14.75) Part - 2
        mRadioGroupQR = findViewById(R.id.radioGroup_QRType);
        mRadioButtonQR = findViewById(R.id.qr_L);
        mQRCodeCommandButton = findViewById(R.id.qrCodeButton);
        //Print downloaded bit image (14.77)
        //mPDBICommandButton = findViewById(R.id.printDownldBitImgButton);
        //Turn white/black reverse print mode on/off (14.78)
        mTurnWBRPOnOffCommandButton = findViewById(R.id.turnBlackWhiteRevPrintModeOnOffButton);
        //Select cut mode and cut paper (14.79)
        mRadioGroupFT = findViewById(R.id.radioGroup_FunctionType);
        mRadioGroupCT = findViewById(R.id.radioGroup_CutType);
        mRadioButtonFT = findViewById(R.id.FT_a);
        mRadioButtonCT = findViewById(R.id.full_cut);
        mSCMCPCommandButton = findViewById(R.id.selectCutModeCutPaperButton);
        mPDF417CommandButton = findViewById(R.id.pdf417Button);

        //asyncRWThread = new AsyncRWThread(getApplicationContext());

        for(int i = 0; i<10; i++){
            mBuffer[i] = (byte) i;
        }

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new SampleAsyntask().execute();
                new createSocket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //connect();
            }
        });

        mBtnCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand(new byte[]{0x0a});
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSocket();
            }
        });


        //Newly Added

        // --- IMPLEMENTATION ---


        //Basic Print Command
        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.printText(mSampleTextEditText.getText().toString());
            }
        });


        mPDFF2FEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_J(mPDFF2FEditText.getText().toString());
            }
        });//Print data and feed a line (14.01)
        mLFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.LF();
            }
        });
        //Print data and feed a page on page mode (14.02)
        mFFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.FF();
            }
        });
        //Print data on page mode (14.03)
        mPDPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_FF();
            }
        });
        //Print data and feed the form to forward (14.04)
        mPDFF2FCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_J(mPDFF2FEditText.getText().toString());
            }
        });
        //Print data and feed n lines (14.05)
        mPDFNLCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_D(mPDFNLEditText.getText().toString());
            }
        });
        mSLFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_3(mSLFEditText.getText().toString());
            }
        });
        mSGRSCCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_SP(mSGRSCEditText.getText().toString());
            }
        });
        //Need to implement parameters
        mSPPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_PM(mSPPMEditText.getText().toString());
            }
        });
        //Need to implement parameters
        mSelectFontCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_M(mSelectFontEditText.getText().toString());
            }
        });
        mPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.PAGE_MODE();
            }
        });

        mSMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.STANDARD_MODE();
            }
        });
        mHTCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.HT();
            }
        });
        //Set the left side margin Command (14.16)
        mSetLeftMarginCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_L(mSetLeftMarginEditText.getText().toString());
            }
        });
        //Set the width of print area Command (14.17)
        mSetWidthOfPrintAreaCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_W(mSetWidthOfPrintAreaEditText.getText().toString());
            }
        });
        //Set the print area on page mode (14.18)
        mSpecifyPrintAreaOnPageModeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMyWifiPrinter.ESC_W(mSPAPMxaxisEditText.getText().toString(),
                        mSPAPMyaxisEditText.getText().toString(),
                        mSPAPMxlengthEditText.getText().toString(),
                        mSPAPMylengthEditText.getText().toString());
            }
        });
        //Set the physical position (14.19)
        mSetPhysicalPositionCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_PP(mSetPhysicalPositionEditText.getText().toString());
            }
        });
        //Set the logical position (14.20)
        mSetLogicalPositionCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_LP(mSetLogicalPositionEditText.getText().toString());
            }
        });
        //Set the vertical physical position in page mode (14.21)
        mSetVerticalPhysicalPositionOnPageModeCommandButton.
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMyWifiPrinter.GS_VPP(mSetVerticalPhysicalPositionOnPageModeEditText.
                                getText().toString());
                    }
                });
        //Set the vertical logical position on page mode (14.22)
        mSetVerticalLogicalPositionOnPageModeCommandButton.
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMyWifiPrinter.GS_VLP(mSetVerticalLogicalPositionOnPageModeEditText.
                                getText().toString());
                    }
                });
        //Print bit image (14.23)
        mPrintBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBitImage();
            }
        });

        //Print raster bit image (14.24)
        mPrintRasterBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickRasterBitImage();
            }
        });
        //Print NV Bit Image (14.25)
        mPrintNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.FS_PP(mSelectPNVBitEditText.getText().toString(),
                        mModePNVBitEditText.getText().toString());
            }
        });
        //Select NV Bit Image (14.25)
        mSelectNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.FS_PS(mSelectSNVBitEditText.getText().toString(),
                        mModeSNVBitEditText.getText().toString(),
                        mOffsetSNVBitEditText.getText().toString());
            }
        });
        //Define NV Bit Image (14.26)
        mDefineNVBitImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Integer.parseInt(mNumberOfNVBitImagesEditText.getText().toString()) > 0) {
                        pickNVBitImage();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Enter number of NV bit Images to be defined",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Log.e("Error", e.getMessage());
                }


            }
        });

        //Select LSB-MSB direction in image (14.27)
        mSelectLSB_MSBDirectionInImageCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.DC2_DIR(mSelectLSB_MSBDirectionInImageEditText.
                        getText().toString());
            }
        });

        //Set print position of HRI characters (14.28)
        mSetPrintPositionOfHRICharCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_H(mSetPrintPositionOfHRICharEditText.getText().toString());
            }
        });

        //Select HRI character size (14.29)
        mSelectHRICharacterSizeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_F(mSelectHRICharacterSizeEditText.getText().toString());
            }
        });

        //Set barcode height (14.30)
        mSetBarcodeHeightCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_h(mSetBarcodeHeightEditText.getText().toString());
            }
        });

        //Set barcode width (14.31)
        mSetBarcodeWidthCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_w(mSetBarcodeWidthEditText.getText().toString());
            }
        });

        //Set N:W aspect of the barcode (14.32)
        mSetNWAspectBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.DC2_ARB(mSetNWAspectBarcodeEditText.getText().toString());
            }
        });

        //Print Barcode (14.33)
        ArrayAdapter<String> barcodeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.barcode_types));
        barcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBarcodeSpinner.setAdapter(barcodeAdapter);

        mBarcodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBarcodeTypeSelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBarcodeTypeSelected = 0;
            }
        });

        mPrintBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mBarcodeTypeSelected) {
                    case 0:
                        mMyWifiPrinter.GS_k(BarcodeType.UPC_A, mBarcodeDataEditText.getText().toString());
                        break;
                    case 1:
                        mMyWifiPrinter.GS_k(BarcodeType.UPC_E, mBarcodeDataEditText.getText().toString());
                        break;
                    case 2:
                        mMyWifiPrinter.GS_k(BarcodeType.JAN13, mBarcodeDataEditText.getText().toString());
                        break;
                    case 3:
                        mMyWifiPrinter.GS_k(BarcodeType.JAN8, mBarcodeDataEditText.getText().toString());
                        break;
                    case 4:
                        mMyWifiPrinter.GS_k(BarcodeType.CODE39, mBarcodeDataEditText.getText().toString());
                        break;
                    case 5:
                        mMyWifiPrinter.GS_k(BarcodeType.ITF, mBarcodeDataEditText.getText().toString());
                        break;
                    case 6:
                        mMyWifiPrinter.GS_k(BarcodeType.CODABAR, mBarcodeDataEditText.getText().toString());
                        break;
                    case 7:
                        mMyWifiPrinter.GS_k(BarcodeType.CODE93, mBarcodeDataEditText.getText().toString());
                        break;
                    default:
                        mMyWifiPrinter.GS_k(BarcodeType.UPC_A, mBarcodeDataEditText.getText().toString());
                        break;
                }
            }
        });

        //Initialize the printer (14.34)
        mInitializePrinterCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_INIT();
            }
        });

        //Set print density (14.40)
        mSetPrintDensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.DC2_PD(mSetPrintDensityEditText.getText().toString());
            }
        });

        /*Cue the marked form(feed the form to print start position)
         * (14.41)
         */
        mGSMFCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_MF();
            }
        });

        //Cut the form (14.43)
        mCutFormCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_I();
            }
        });

        //Inform system time of the host (14.51)
        mInformSysTimeOfHostCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] a = new byte[6];
                a[0] = (byte) Calendar.getInstance().get(Calendar.SECOND);//d1 - second
                a[1] = (byte) Calendar.getInstance().get(Calendar.MINUTE);//d2 - minute
                a[2] = (byte) Calendar.getInstance().get(Calendar.HOUR);//d3 - hour
                a[3] = (byte) Calendar.getInstance().get(Calendar.DAY_OF_MONTH);//d4 - day
                a[4] = (byte) Calendar.getInstance().get(Calendar.MONTH);//d5 - month
                a[5] = (byte) Calendar.getInstance().get(Calendar.YEAR);//d6 - year

                mMyWifiPrinter.GS_i(a);
            }
        });

        //Download the firmware (14.52)

        //Feed the form in pixels (14.54)
        mFFIPIXELSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_d(mFFIPIXELSEditText.getText().toString());
            }
        });

        //Turn Underline button ON/OFF (14.70)
        mtUOnOffCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_hyphen(mUnderLineEditText.getText().toString());
            }
        });
        //Select default line spacing (14.71)
        mSDLSCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_2();
            }
        });

        //Turn emphasized mode on/off (14.72)
        mTEMOnOffCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_E(mTEMOnOffEditText.getText().toString());
            }
        });

        //Select print direction in page mode (14.73)
        mSPDPMCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_T(mSPDPMEditText.getText().toString());
            }
        });

        //Select justification (14.74)
        mSJCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.ESC_a(mSJEditText.getText().toString());
            }
        });

        //Set up and print the symbol (14.75)

        //Print QR Code (14.75) Part - 2
        mQRCodeCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioButtonQR.getText().toString().equals("L")) {
                    mMyWifiPrinter.QrCode(mQRSizeEditText.getText().toString(), QRErrCorrLvl.L,
                            mQRUserDataEditText.getText().toString());
                } else if (mRadioButtonQR.getText().toString().equals("M")) {
                    mMyWifiPrinter.QrCode(mQRSizeEditText.getText().toString(), QRErrCorrLvl.M,
                            mQRUserDataEditText.getText().toString());
                } else if (mRadioButtonQR.getText().toString().equals("Q")) {
                    mMyWifiPrinter.QrCode(mQRSizeEditText.getText().toString(), QRErrCorrLvl.Q,
                            mQRUserDataEditText.getText().toString());
                } else if (mRadioButtonQR.getText().toString().equals("H")) {
                    mMyWifiPrinter.QrCode(mQRSizeEditText.getText().toString(), QRErrCorrLvl.H,
                            mQRUserDataEditText.getText().toString());
                }
            }
        });

        //Turn white/black reverse print mode on/off (14.78)
        mTurnWBRPOnOffCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.GS_B(mTurnWBRPOnOffEditText.getText().toString());
            }
        });

        //Select cut mode and cut paper (14.79)
        mSCMCPCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioButtonCT.getText().toString().equals("Full Cut")) {
                    if (mRadioButtonFT.getText().toString().equals("A")) {
                        mMyWifiPrinter.GS_V(FunctionType.A, CutCommand.FULLCUT, mSCMCPEditText.getText().toString());
                    } else if (mRadioButtonFT.getText().toString().equals("B")) {
                        mMyWifiPrinter.GS_V(FunctionType.B, CutCommand.FULLCUT, mSCMCPEditText.getText().toString());
                    } else if (mRadioButtonFT.getText().toString().equals("C")) {
                        mMyWifiPrinter.GS_V(FunctionType.C, CutCommand.FULLCUT, mSCMCPEditText.getText().toString());
                    }
                } else if (mRadioButtonCT.getText().toString().equals("Partial Cut")) {
                    if (mRadioButtonFT.getText().toString().equals("A")) {
                        mMyWifiPrinter.GS_V(FunctionType.A, CutCommand.PARTIALCUT, mSCMCPEditText.getText().toString());
                    } else if (mRadioButtonFT.getText().toString().equals("B")) {
                        mMyWifiPrinter.GS_V(FunctionType.B, CutCommand.PARTIALCUT, mSCMCPEditText.getText().toString());
                    } else if (mRadioButtonFT.getText().toString().equals("C")) {
                        mMyWifiPrinter.GS_V(FunctionType.C, CutCommand.PARTIALCUT, mSCMCPEditText.getText().toString());
                    }
                }
            }
        });


        //Basic Print Command
        mPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.printText(mSampleTextEditText.getText().toString());
                //printer.printAdditionalBarcode(ExtraBarcodeType.CODE128, "500", "300",
                //       "0905", ImageMode.NORMAL);
                //TODO: UI
                /*printer.printPDF417("5","24",
                        PDF417ErrorCorrectionMode.LEVEL,
                        PDF417ErrorCorrectionLevel.LEVEL0,
                        PDF417Options.STANDARD,
                        "Hello there.. Karthik");*/

                //printer.code93(BarcodeType.CODE93,"ABCDEFG");
                //printer.code128(BarcodeType.CODE128,mSampleTextEditText.getText().toString(),
                //CODE128Subset.SUBSETC);


            }
        });

        //PDF417 Command
        mPDF417CommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWifiPrinter.printPDF417("3",
                        "10",
                        PDF417ErrorCorrectionMode.LEVEL,
                        PDF417ErrorCorrectionLevel.LEVEL0,
                        PDF417Options.STANDARD,
                        mSampleTextEditText.getText().toString());
            }
        });

    }

    private void closeSocket() {
        //TODO: Check if close lock is useful.
        try {
            if(mSocket.isConnected() && !mSocket.isClosed()) {

                mSocket.close();
                mSocketState = false;
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(),
                        "Socket Closed!",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendCommand(byte[] bytes) {
        new sendCommandATask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bytes);
    }

    private void connect() {

    }

    private class SampleAsyntask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),
                    "onPreExecute() Called!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i<10 ; i++){
                System.out.println(i);
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(!isCancelled()){
                Toast.makeText(getApplicationContext(),
                        "Value : " + values[0].toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),
                    "Asyntask Completed!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class sendCommandATask extends AsyncTask<byte[], Boolean, String>{

        @Override
        protected String doInBackground(byte[]... bytes) {

            try {
                mBufferedOutputStream.write(bytes[0]);
                mBufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                mSocketState = false;
                invalidateOptionsMenu();
                return e.getMessage();
            }
            return "Success!";
        }

        @Override
        protected void onPostExecute(String s) {
            if(!s.equals("Success!"))
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    private class createSocket extends AsyncTask<Void, Boolean, String>{

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),
                    "Attempting Socket Connection...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            //Create a socket connection

            mSocket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("192.168.4.1", 23);

            try {
                mSocket.connect(socketAddress, 2000);

                if(mSocket.isConnected()){
                    mBufferedInputStream = new BufferedInputStream(mSocket.getInputStream());
                    mBufferedOutputStream = new BufferedOutputStream(mSocket.getOutputStream());
                    Log.d("Receive Buffer Size:" , String.valueOf(mSocket.getReceiveBufferSize()));
                    Log.d("Send Buffer Size:" , String.valueOf(mSocket.getSendBufferSize()));
                    Log.d("Input Shutdown:" , String.valueOf(mSocket.isInputShutdown()));
                    Log.d("Output Shutdown:" , String.valueOf(mSocket.isOutputShutdown()));
                    publishProgress(Boolean.TRUE);
                    mSocketState = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
                mSocketState = false;
                invalidateOptionsMenu();
                publishProgress(Boolean.FALSE);
                return e.getMessage();
            }
            invalidateOptionsMenu();
            return "Socket Connection Successful...";
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            Toast.makeText(getApplicationContext(),
                    "Wifi State:"+ values[0],
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_appbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.close:
                // Close the application
                this.finish();
                return true;
            case R.id.wifi_state:
                // Current state of the Wifi
                Toast.makeText(getApplicationContext(),
                        "Wifi State...",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        MenuItem menuItem = menu.getItem(0);

        if(mSocketState){
            menuItem.setIcon(R.drawable.ic_thumb_up_black_24dp);
        }
        else{
            menuItem.setIcon(R.drawable.ic_thumb_down_black_24dp);
        }
        return true;
    }

    //Newly Added

    //TODO Control Transfer
        /*mControlTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] dataReceivedFromPrinter = new byte[16];  //For Understanding purpose only
                dataReceivedFromPrinter = mMyWifiPrinter.controlTransfer();
                Toast.makeText(getApplicationContext(), dataReceivedFromPrinter.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });*/
        /*
        Change button to btn
         */

    /**
     * Broadcast Receiver to listen for all the actions specified in the Intent Filter
     */


    /**
     * Finish the Wifi connection by releasing all the resources and go to MainActivity
     */
    private void closeIntent(){
        mMyWifiPrinter.close();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    /***
     * Supporting Methods
     */
    private void pickBitImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",false);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 1);
    }

    private void pickRasterBitImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",true);
        intent.putExtra("return-data",true);
        startActivityForResult(intent, 2);
    }

    private void pickNVBitImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        //intent.putExtra("return-data",true);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }
        //TODO - Bit Image Selection
        if(requestCode == 1){
            final Bundle extras = data.getExtras();
            if(extras != null){
                //Get Image from Gallery
                //mBmp = extras.getParcelable("data");
            }
        }
        //Raster Bit Image Selection
        else if(requestCode == 2){
            final Bundle extras = data.getExtras();
            if(extras != null){
                //Get Image from Gallery
                Bitmap bmp = extras.getParcelable("data");
                mMyWifiPrinter.GS_v(bmp, mModeOfRasterBitEditText.getText().toString());
            }
        }
        //NV Bit Image Selection
        else if(requestCode == 3){
            /**
             * Case 1: Number of NV Bit Images = 1
             * Case 2: Number of NV Bit Images > 1
             */
            //Case 1:
            if(data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.
                            getBitmap(getApplicationContext().getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMyWifiPrinter.FS_Q(new Bitmap[]{bitmap}, mNumberOfNVBitImagesEditText.
                        getText().toString());
            }
            //Case 2:
            else if(data.getClipData() != null){

                ClipData clipData = data.getClipData();
                int numberOfImages = clipData.getItemCount();
                Bitmap[] bitmapArray = new Bitmap[numberOfImages];
                for(int i = 0; i < numberOfImages; i++){
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    try{
                        bitmapArray[i] = MediaStore.Images.Media.
                                getBitmap(getApplicationContext().getContentResolver(),uri);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                mMyWifiPrinter.FS_Q(bitmapArray, mNumberOfNVBitImagesEditText.
                        getText().toString());
            }
            else{
                Toast.makeText(getApplicationContext(),"Invalid!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Callback method for Radiogroup - Function Type
     * @param view - Radiobutton selected
     */
    public void RadioButtonFuncTypeSelected(View view){
        int radioButtonId = mRadioGroupFT.getCheckedRadioButtonId();
        mRadioButtonFT = findViewById(radioButtonId);

        Toast.makeText(this, "Function Mode: "+ mRadioButtonFT.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback method for Radiogroup - Cut Type
     * @param view
     */
    public void RadioButtonCutTypeSelected(View view){
        int radioButtonId = mRadioGroupCT.getCheckedRadioButtonId();
        mRadioButtonCT = findViewById(radioButtonId);

        Toast.makeText(this, "Cut Mode: "+ mRadioButtonCT.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback method for Radiogroup - QR Correction Type
     * @param view
     */
    public void RadioButtonQRTypeSelected(View view){
        int radioButtonId = mRadioGroupQR.getCheckedRadioButtonId();
        mRadioButtonQR = findViewById(radioButtonId);

        Toast.makeText(this, "QR Correction Level: "+ mRadioButtonQR.getText(), Toast.LENGTH_SHORT).show();
    }
}





