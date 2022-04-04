class WinnerFragment : Fragment() {

    private var _binding: FragmentWinnerBinding? = null
    private val binding get() = _binding!!

    private var mInterstitialAd: InterstitialAd? = null
    private val mWinnerViewModel: WinnerViewModel by viewModels()
  
    private var TAG = "MyWinnerFragment"
    private var mAdIsLoading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {    
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_winner, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            model = mWinnerViewModel
            lifecycleOwner = viewLifecycleOwner
            frag = this@WinnerFragment
        }
        mWinnerViewModel.init(args.aciertos)
        loadAd()
    }

    fun mContinue() {
        showInterstitial()
        view!!.findNavController().navigate(R.id.action_winnerFragment_to_quizzFragment)
    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    mAdIsLoading = false
                    Log.d(TAG, "onFailed")
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mAdIsLoading = false
                    Log.d(TAG, "Add was loaded")
                }
            }
        )
    }

    private fun showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    mInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.d(TAG, "Ad failed to show.")
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                }
            }
            mInterstitialAd?.show(requireActivity())
        } else {
            Log.d(TAG, "Ad doesn't load.")
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
